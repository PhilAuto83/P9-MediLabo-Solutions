package com.phildev.front.mls.error;

import feign.Response;
import feign.codec.ErrorDecoder;

import static feign.FeignException.errorStatus;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400 ) {
            return new BadRequestException(response.reason());

        }else if(response.status() == 404) {
            return new ResponseNotFoundException(response.reason());
        }else if(response.status()>=500 && response.status()<=599){
            return new ServerResponseException("Le serveur ne répond pas. Veuillez réessayer ultérieurement.");
        }
        return errorStatus(methodKey, response);
    }


}
