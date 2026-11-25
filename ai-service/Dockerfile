
FROM python:3.11-slim

WORKDIR /app

# Системные зависимости.
RUN apt-get update && apt-get install -y --no-install-recommends \
    libgl1 \
    libglib2.0-0 \
    git \
    && rm -rf /var/lib/apt/lists/*

# CPU-only PyTorch.
RUN pip install --no-cache-dir \
    torch==2.6.0 \
    torchvision==0.21.0 \
    --index-url https://download.pytorch.org/whl/cpu

# Python dependencies.
COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

# Исходники.
COPY . .

# Единый каталог для кеширования моделей.
ENV HOME=/app
ENV HF_HOME=/app/.cache/huggingface
ENV TRANSFORMERS_CACHE=/app/.cache/huggingface
ENV YOLO_CONFIG_DIR=/app/.cache/Ultralytics

# Параметры YOLO.
ENV YOLO_MODEL=yoloe-26n-seg.pt
ENV YOLO_CONFIDENCE=0.15
ENV YOLO_IMAGE_SIZE=640

# Мобильный процессор: не перегружаем CPU потоками.
ENV OMP_NUM_THREADS=2
ENV MKL_NUM_THREADS=2

RUN mkdir -p /app/.cache

ENV PYTHONPATH=/app
WORKDIR /app/.cache

EXPOSE 8000

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
