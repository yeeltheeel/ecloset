import base64
import io
from contextlib import asynccontextmanager

from fastapi import FastAPI, File, UploadFile, HTTPException
from PIL import Image

from sam_segmenter import SamSegmenter
from category_classifier import CategoryClassifier
from color_extractor import ColorExtractor

# Глобальные экземпляры (инициализация при старте)
sam: SamSegmenter | None = None
classifier: CategoryClassifier | None = None
color_extractor: ColorExtractor | None = None

@asynccontextmanager
async def lifespan(app: FastAPI):
    global sam, classifier, color_extractor
    print("Loading SAM 2 (tiny)...")
    sam = SamSegmenter()
    print("Loading Fashion-CLIP...")
    classifier = CategoryClassifier()
    color_extractor = ColorExtractor()
    print("AI service ready.")
    yield
    # cleanup не нужен

app = FastAPI(lifespan=lifespan, title="Ecloset AI Service")

@app.get("/health")
def health():
    return {"status": "ok"}

@app.post("/analyze")
async def analyze(image: UploadFile = File(...)):
    # 1. Читаем и валидируем
    contents = await image.read()
    if not contents:
        raise HTTPException(400, "Empty file")
    try:
        pil_image = Image.open(io.BytesIO(contents))
        pil_image.verify()
        pil_image = Image.open(io.BytesIO(contents))  # reopen после verify
    except Exception as e:
        raise HTTPException(400, f"Invalid image: {e}")

    try:
        # 2. SAM 2 — сегментация
        segmented = sam.segment(pil_image)

        # 3. Сохраняем PNG в base64
        buf = io.BytesIO()
        segmented.save(buf, format="PNG")
        processed_b64 = base64.b64encode(buf.getvalue()).decode("utf-8")

        # 4. Fashion-CLIP — категория
        category, confidence = classifier.classify(pil_image)

        # 5. K-Means — цвета (по сегментированному изображению)
        colors = color_extractor.extract(segmented, k=3)

        return {
            "processedImageBase64": processed_b64,
            "category": category,
            "categoryConfidence": confidence,
            "colorsHex": colors,
        }
    except Exception as e:
        raise HTTPException(500, f"Processing error: {e}")