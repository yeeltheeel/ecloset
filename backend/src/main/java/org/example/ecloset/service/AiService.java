package org.example.ecloset.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.ai.AiAnalysisResult;
import org.example.ecloset.exception.AiServiceException;
import org.example.ecloset.exception.FileStorageException;
import org.example.ecloset.exception.NoClothingDetectedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AiService {

    /** PNG в base64 легко весит несколько МБ; дефолтный лимит буфера WebClient — 256 КБ. */
    private static final int MAX_RESPONSE_SIZE = 16 * 1024 * 1024;

    @Value("${app.ai.base-url}")
    private String aiBaseUrl;

    @Value("${app.ai.timeout-sec:120}")
    private long timeoutSec;

    private final WebClient.Builder webClientBuilder;

    private WebClient webClient;

    @PostConstruct
    void init() {
        this.webClient = webClientBuilder
                .baseUrl(aiBaseUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(MAX_RESPONSE_SIZE))
                .build();
    }

    public AiAnalysisResult analyze(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new FileStorageException("Файл пустой");
        }

        try {
            byte[] bytes = image.getBytes();
            String filename = image.getOriginalFilename() != null
                    ? image.getOriginalFilename()
                    : "image.jpg";
            MediaType contentType = MediaType.parseMediaType(
                    image.getContentType() != null ? image.getContentType() : "image/jpeg");

            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("image", new ByteArrayResource(bytes) {
                @Override
                public String getFilename() { return filename; }
            }).contentType(contentType);

            AiAnalysisResult result = webClient
                    .post()
                    .uri("/analyze")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(bodyBuilder.build())
                    .retrieve()
                    // Питон: 422 = на фото не найдена вещь → клиенту 422, а не 503.
                    .onStatus(status -> status.value() == 422, response ->
                            response.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .map(body -> new NoClothingDetectedException(
                                            "На фото не найдена вещь. Попробуйте другое фото.")))
                    // Питон: 400 = пустой/битый файл → ошибка клиента, не недоступность AI.
                    .onStatus(status -> status.value() == 400, response ->
                            response.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .map(body -> new FileStorageException("Некорректный файл изображения")))
                    .bodyToMono(AiAnalysisResult.class)
                    .block(Duration.ofSeconds(timeoutSec));

            if (result == null) {
                throw new AiServiceException("AI-сервис вернул пустой ответ");
            }
            return result;
        } catch (IOException e) {
            throw new AiServiceException("Не удалось прочитать изображение", e);
        } catch (NoClothingDetectedException | FileStorageException | AiServiceException e) {
            // Пробрасываем как есть, чтобы 422/400 не превратились в общее 503.
            throw e;
        } catch (Exception e) {
            throw new AiServiceException("Ошибка при обращении к AI-сервису", e);
        }
    }
}