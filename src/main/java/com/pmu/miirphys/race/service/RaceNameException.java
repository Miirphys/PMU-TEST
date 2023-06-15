package com.pmu.miirphys.race.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

/**
 * @Author ponthiaux.eric@gmail.com
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RaceNameException extends Exception {

    public RaceNameException(String name) {

        super(MessageFormat.format(RaceExceptionMessages.INCORRECT_RACE_NAME, name));

    }
}
