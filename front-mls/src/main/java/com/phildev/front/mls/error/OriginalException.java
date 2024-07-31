package com.phildev.front.mls.error;

import lombok.Data;

@Data
public class OriginalException {

    private String date;
    private String method;
    private String statusCode;
    private String path;
    private String message;

}
