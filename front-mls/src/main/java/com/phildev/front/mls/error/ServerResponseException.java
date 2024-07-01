package com.phildev.front.mls.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerResponseException extends RuntimeException{

    public ServerResponseException(String message){
        super(message);
    }

}
