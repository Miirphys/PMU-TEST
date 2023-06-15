package com.pmu.miirphys.race.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

/**
 * @Author ponthiaux.eric@gmail.com
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RaceNotFoundException extends Exception {

    public RaceNotFoundException(Long raceId) {

        super(MessageFormat.format(RaceExceptionMessages.RACE_NOT_FOUND, raceId));

    }
}
