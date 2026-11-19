FROM python:3.11-slim

WORKDIR /app

# Системные зависимости для OpenCV и torch
RUN apt-get update && apt-get install -y --no-install-recommends \
    libgl1   \
    libglib2.0-0 \
    git \
    && rm -rf /var/lib/apt/lists/*


RUN pip install --no-cache-dir torch torchvision --index-url https://download.pytorch.org/whl/cpu
RUN pip cache purge
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

RUN pip install --no-cache-dir --no-deps "sam-2 @ git+https://github.com/facebookresearch/sam2.git" \
    && pip install --no-cache-dir iopath hydra-core

COPY . .

# Кеш моделей Hugging Face внутри образа (опционально, но ускоряет старт)
ENV HF_HOME=/app/.cache/huggingface
ENV TRANSFORMERS_CACHE=/app/.cache/huggingface

EXPOSE 8000
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]