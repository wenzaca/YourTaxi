package com.yourtaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The select car is already in use.")
public class CarAlreadyInUseException extends Exception
{

    public CarAlreadyInUseException(String message)
    {
        super(message);
    }

}
