
import base64
import io
import logging
from contextlib import asynccontextmanager

from fastapi import FastAPI, File, UploadFile, HTTPException
from PIL import Image, ImageOps
from starlette.concurrency import run_in_threadpool

from yolo_segmenter import YoloSegmenter
from category_classifier import CategoryClassifier
from color_extractor import ColorExtractor


logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


# Глобальные экземпляры моделей.
segmenter: YoloSegmenter | None = None
classifier: CategoryClassifier | None = None
color_extractor: ColorExtractor | None = None


@asynccontextmanager
async def lifespan(app: FastAPI):
    global segmenter, classifier, color_extractor

    logger.info("Loading YOLOE-26-Seg...")
    segmenter = YoloSegmenter()

    logger.info("Loading Fashion-CLIP...")
    classifier = CategoryClassifier()

    logger.info("Initializing ColorExtractor...")
    color_extractor = ColorExtractor()

    logger.info("AI service ready.")

    yield

    logger.info("AI service shutting down.")


app = FastAPI(
    lifespan=lifespan,
    title="Ecloset AI Service",
)


@app.get("/health")
def health():
    ready = (
        segmenter is not None
        and classifier is not None
        and color_extractor is not None
    )

    return {
        "status": "ok" if ready else "starting"
    }


def process_image(pil_image: Image.Image) -> dict:
    """
    Полный pipeline обработки фотографии.
    """

    if segmenter is None or classifier is None or color_extractor is None:
        raise RuntimeError("AI models are not initialized")

    # 1. YOLOE — сегментация одежды.
    segmented = segmenter.segment(pil_image)

    # 2. Сохраняем обработанное изображение в PNG.
    buffer = io.BytesIO()
    segmented.save(buffer, format="PNG")

    processed_b64 = base64.b64encode(
        buffer.getvalue()
    ).decode("utf-8")

    # 3. Fashion-CLIP — определение категории.
    category, confidence = classifier.classify(pil_image)

    # 4. K-Means — извлечение цветов.
    # Прозрачный фон автоматически игнорируется.
    colors = color_extractor.extract(segmented, k=3)

    return {
        "processedImageBase64": processed_b64,
        "category": category,
        "categoryConfidence": confidence,
        "colorsHex": colors,
    }


@app.post("/analyze")
async def analyze(image: UploadFile = File(...)):

    # 1. Проверяем загруженный файл.
    contents = await image.read()

    if not contents:
        raise HTTPException(
            status_code=400,
            detail="Empty file",
        )

    try:
        pil_image = Image.open(io.BytesIO(contents))
        pil_image.verify()

        # Повторно открываем после verify().
        pil_image = Image.open(io.BytesIO(contents))
        pil_image = ImageOps.exif_transpose(pil_image)
        pil_image.load()

    except Exception:
        raise HTTPException(
            status_code=400,
            detail="Invalid image",
        )

    # 2. Запускаем обработку в отдельном потоке.
    try:
        result = await run_in_threadpool(
            process_image,
            pil_image,
        )

        return result

    except ValueError as e:
        logger.info("Segmentation failed: %s", e)

        raise HTTPException(
            status_code=422,
            detail=str(e),
        )

    except Exception:
        logger.exception("Image processing failed")

        raise HTTPException(
            status_code=500,
            detail="Image processing error",
        )
