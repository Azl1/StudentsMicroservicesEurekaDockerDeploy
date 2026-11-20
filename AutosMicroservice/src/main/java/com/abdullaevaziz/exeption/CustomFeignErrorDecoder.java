package com.abdullaevaziz.exeption;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomFeignErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response.body() != null) {
                String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
                String message = body; // По умолчанию — весь текст

                // Пытаемся распарсить как JSON, но только если тело начинается с {
                if (body.trim().startsWith("{")) {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(body);
                        if (jsonNode.has("message")) {
                            message = jsonNode.get("message").asText();
                        } else if (jsonNode.has("error")) {
                            message = jsonNode.get("error").asText();
                        }
                    } catch (Exception e) {
                        // Если не удалось распарсить — оставляем текст как есть
                    }
                }

                // Возвращаем статус, соответствующий ответу
                if (response.status() >= 400 && response.status() < 500) {
                    return new IllegalArgumentException(message);
                } else if (response.status() >= 500) {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), message);
                }
                return new IllegalArgumentException(message);
            }
        } catch (IOException e) {
            return new RuntimeException("Failed to read feign error body", e);
        }
        return FeignException.errorStatus(methodKey, response);
    }
}