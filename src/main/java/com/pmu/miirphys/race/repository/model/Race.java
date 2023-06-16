package com.pmu.miirphys.race.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @Author ponthiaux.eric@gmail.com
 */

@Entity
public class Race {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Setter
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotBlank(message = "Date is mandatory")
    private LocalDate date;

}
