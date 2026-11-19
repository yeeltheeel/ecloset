import numpy as np
import torch
from PIL import Image
from sam2.sam2_image_predictor import SAM2ImagePredictor

class SamSegmenter:
    def __init__(self, model_name: str = "facebook/sam2-hiera-tiny"):
        # tiny 
        self.predictor = SAM2ImagePredictor.from_pretrained(model_name, device="cpu")
        self.device = "cuda" if torch.cuda.is_available() else "cpu"
        self.predictor.model.to(self.device)

    def segment(self, image: Image.Image) -> Image.Image:
        """
        Возвращает RGBA-изображение с прозрачным фоном.
        Использует автоматическую сегментацию (без prompts).
        """
        image_rgb = image.convert("RGB")
        np_image = np.array(image_rgb)

        with torch.inference_mode():
            self.predictor.set_image(np_image)
            # multimask_output=True вернёт несколько масок; берём самую "уверенную"
            masks, scores, _ = self.predictor.predict(multimask_output=True)

        # scores — массив уверенности для каждой маски
        best_idx = int(np.argmax(scores))
        best_mask = masks[best_idx] > 0

        # Собираем RGBA
        rgba = np.array(image_rgb.convert("RGBA"))
        rgba[:, :, 3] = (best_mask * 255).astype(np.uint8)
        return Image.fromarray(rgba, mode="RGBA")