
import os

import numpy as np
from PIL import Image
from ultralytics import YOLOE


class YoloSegmenter:
    """
    Сегментация одежды с помощью YOLOE-26-Seg.

    Возвращает RGBA-изображение с прозрачным фоном.
    Для фотографии выбирает один наиболее уверенно обнаруженный предмет.
    """

    CLOTHING_PROMPTS = [
        "shirt",
        "t-shirt",
        "top",
        "blouse",
        "pants",
        "trousers",
        "jeans",
        "shorts",
        "dress",
        "skirt",
        "jacket",
        "coat",
        "hoodie",
        "sweater",
        "sweatshirt",
        "cardigan",
        "clothing",
        "garment",
    ]

    def __init__(
        self,
        model_name: str | None = None,
        confidence: float = 0.15,
        image_size: int = 640,
    ):
        model_name = model_name or os.getenv(
            "YOLO_MODEL", "yoloe-26n-seg.pt"
        )

        self.confidence = float(
            os.getenv("YOLO_CONFIDENCE", confidence)
        )
        self.image_size = int(
            os.getenv("YOLO_IMAGE_SIZE", image_size)
        )

        print(f"Loading YOLOE model: {model_name}")

        # Все вычисления выполняются на CPU.
        self.model = YOLOE(model_name)

        self.model.set_classes(self.CLOTHING_PROMPTS)

        print("YOLOE initialized successfully.")

    def segment(self, image: Image.Image) -> Image.Image:
        """
        Принимает PIL Image.
        Возвращает RGBA с прозрачным фоном.

        Выбирает маску с максимальной уверенностью.
        """

        image_rgb = image.convert("RGB")
        image_np = np.asarray(image_rgb)

        results = self.model.predict(
            source=image_np,
            device="cpu",
            imgsz=self.image_size,
            conf=self.confidence,
            iou=0.5,
            max_det=10,
            retina_masks=True,
            verbose=False,
        )

        if not results:
            raise ValueError("No clothing item detected")

        result = results[0]

        if result.masks is None or result.boxes is None:
            raise ValueError("No clothing segmentation masks found")

        masks = result.masks.data
        scores = result.boxes.conf

        if len(masks) == 0 or len(scores) == 0:
            raise ValueError("No clothing item detected")

        # Находим наиболее уверенное обнаружение.
        best_idx = int(scores.argmax().item())

        mask = masks[best_idx].cpu().numpy()

        # Бинарная маска.
        mask = (mask > 0.5).astype(np.uint8) * 255

        # На случай, если размер маски отличается от исходного.
        if mask.shape != (image_rgb.height, image_rgb.width):
            mask_image = Image.fromarray(mask, mode="L")
            mask_image = mask_image.resize(
                image_rgb.size,
                Image.Resampling.NEAREST,
            )
            mask = np.asarray(mask_image)

        # Формируем RGBA.
        rgba = np.array(image_rgb.convert("RGBA"))
        rgba[:, :, 3] = mask

        return Image.fromarray(rgba, mode="RGBA")
