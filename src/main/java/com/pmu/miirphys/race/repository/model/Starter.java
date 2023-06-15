package com.pmu.miirphys.race.repository.model;


import jakarta.persistence.*;
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
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

}