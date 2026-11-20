package com.abdullaevaziz.exeption;

import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ErrorResponseDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // Если ошибка 400 (Bad Request)
        if (response.status() == 400) {
            try {
                // Читаем тело ответа (JSON с ошибкой)
                String body = new BufferedReader(
                        new InputStreamReader(response.body().asInputStream())
                ).lines().collect(Collectors.joining("\n"));
                System.out.println(body);
                // Возвращаем как RuntimeException
                return new RuntimeException(body);

            } catch (Exception e) {
                return new RuntimeException("Ошибка при вызове другого сервиса");
            }
        }

        // Для других ошибок
        return new RuntimeException("Ошибка: " + response.status());
    }
}
