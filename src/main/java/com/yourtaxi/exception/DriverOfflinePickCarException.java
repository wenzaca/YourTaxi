package com.yourtaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The rating value must be between 0 and 5.")
public class DriverOfflinePickCarException extends Exception
{

    public DriverOfflinePickCarException(String message)
    {
        super(message);
    }

}
