package com.pmu.miirphys.race.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;
/**
 * @Author ponthiaux.eric@gmail.com
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StarterNotFoundException extends Exception {

    public StarterNotFoundException(Long starterId) {

        super(MessageFormat.format(RaceExceptionMessages.STARTER_NOT_FOUND, starterId));

    }
}
