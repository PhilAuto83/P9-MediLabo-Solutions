package com.phildev.front.mls.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        OriginalException originalException = null;
        try (InputStream body = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            originalException = mapper.readValue(body, OriginalException.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        return switch (response.status()) {
            case 400 ->
                    new BadRequestException(originalException.getMessage() != null ? originalException.getMessage() : "Bad Request");
            case 404 ->
                    new ResponseNotFoundException(originalException.getMessage() != null ? originalException.getMessage() : "Not found");
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}
