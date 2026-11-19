package org.example.ecloset.service;

import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.ai.AiAnalysisResult;
import org.example.ecloset.exception.AiServiceException;
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

    @Value("${app.ai.base-url}")
    private String aiBaseUrl;

    @Value("${app.ai.timeout-sec:120}")
    private long timeoutSec;

    private final WebClient.Builder webClientBuilder;

    public AiAnalysisResult analyze(MultipartFile image) {
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

            AiAnalysisResult result = webClientBuilder
                    .baseUrl(aiBaseUrl)
                    .build()
                    .post()
                    .uri("/analyze")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(bodyBuilder.build())
                    .retrieve()
                    .bodyToMono(AiAnalysisResult.class)
                    .block(Duration.ofSeconds(timeoutSec));

            if (result == null) {
                throw new AiServiceException("AI-сервис вернул пустой ответ");
            }
            return result;
        } catch (IOException e) {
            throw new AiServiceException("Не удалось прочитать изображение", e);
        } catch (AiServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new AiServiceException("Ошибка при обращении к AI-сервису", e);
        }
    }
}