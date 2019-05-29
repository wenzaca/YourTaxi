package com.yourtaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The rating value must be between 0 and 5.")
public class InvalidRatingValueException extends Exception
{

    public InvalidRatingValueException(String message)
    {
        super(message);
    }

}
