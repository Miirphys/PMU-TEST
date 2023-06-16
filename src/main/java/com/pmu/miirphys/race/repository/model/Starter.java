package com.pmu.miirphys.race.repository.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ponthiaux.eric@gmail.com
 */

@Entity
public class Starter {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

}