from transformers import AutoProcessor, AutoModelForZeroShotImageClassification
from PIL import Image
import torch

# Маппинг: строка для Fashion-CLIP → значение enum Category в Java
CATEGORY_LABELS = {
    "a photo of a jacket": "JACKET",
    "a photo of a t-shirt": "T_SHIRT",
    "a photo of a shirt": "SHIRT",
    "a photo of pants": "PANTS",
    "a photo of jeans": "JEANS",
    "a photo of a dress": "DRESS",
    "a photo of a skirt": "SKIRT",
    "a photo of shorts": "SHORTS",
    "a photo of a sweater": "SWEATER",
    "a photo of a hoodie": "HOODIE",
    "a photo of a coat": "COAT",
    "a photo of shoes": "SHOES",
    "a photo of an accessory": "ACCESSORIES",
}

class CategoryClassifier:
    def __init__(self, model_name: str = "patrickjohncyh/fashion-clip"):
        self.processor = AutoProcessor.from_pretrained(model_name)
        self.model = AutoModelForZeroShotImageClassification.from_pretrained(model_name)
        self.model.eval()

    def classify(self, image: Image.Image) -> tuple[str | None, float | None]:
        """
        Возвращает (category_enum_name, confidence) или (None, None).
        """
        labels = list(CATEGORY_LABELS.keys())
        inputs = self.processor(
            text=labels, images=image.convert("RGB"),
            return_tensors="pt", padding=True
        )

        with torch.no_grad():
            outputs = self.model(**inputs)

        probs = outputs.logits_per_image.softmax(dim=1)[0]
        best_idx = int(torch.argmax(probs))
        confidence = float(probs[best_idx])

        best_label = labels[best_idx]
        return CATEGORY_LABELS[best_label], confidence