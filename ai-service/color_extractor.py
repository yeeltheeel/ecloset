import numpy as np
from PIL import Image
from sklearn.cluster import KMeans

class ColorExtractor:
    def extract(self, image: Image.Image, k: int = 3) -> list[str]:
        """
        Возвращает список HEX-цветов, отсортированных по площади.
        Игнорирует прозрачные и почти белые/чёрные пиксели.
        """
        # Берём RGBA, чтобы отбросить прозрачный фон
        rgba = np.array(image.convert("RGBA"))
        mask = rgba[:, :, 3] > 128
        pixels = rgba[:, :, :3][mask]

        if len(pixels) < k:
            return []

        # Downsample для скорости
        if len(pixels) > 10000:
            idx = np.random.choice(len(pixels), 10000, replace=False)
            pixels = pixels[idx]

        # Конвертируем в float32 для K-Means
        pixels = pixels.astype(np.float32)

        kmeans = KMeans(n_clusters=k, random_state=42, n_init="auto")
        kmeans.fit(pixels)

        # Считаем площади кластеров
        labels = kmeans.labels_
        counts = np.bincount(labels, minlength=k)
        order = np.argsort(-counts)

        hex_colors = []
        for idx in order:
            center = kmeans.cluster_centers_[idx]
            r, g, b = [int(round(c)) for c in center]
            # Пропускаем почти белый/чёрный, если есть другие цвета
            if len(hex_colors) < k and (r + g + b) > 30:
                hex_colors.append(f"#{r:02X}{g:02X}{b:02X}")

        return hex_colors[:k]