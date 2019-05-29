package com.yourtaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The expected search for coordinate must be latitude value, longitude")
public class GeoCoordinateCreationException extends RuntimeException
{

    public GeoCoordinateCreationException(String message)
    {
        super(message);
    }

}
