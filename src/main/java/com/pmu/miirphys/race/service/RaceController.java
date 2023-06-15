package com.pmu.miirphys.race.service;

import com.pmu.miirphys.race.repository.model.Starter;
import com.pmu.miirphys.race.repository.model.Race;
import com.pmu.miirphys.race.repository.StarterRepository;
import com.pmu.miirphys.race.repository.RaceRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.pmu.miirphys.race.service.RaceControllerConstants.MESSAGE_BUS_END_RACE_TOPIC;
import static com.pmu.miirphys.race.service.RaceControllerConstants.MESSAGE_BUS_START_RACE_TOPIC;

/**
 * @Author ponthiaux.eric@gmail.com
 */

@RestController
@RequestMapping("/races")
public class RaceController {

    private final RaceRepository raceRepository;
    private final StarterRepository starterRepository;
    private final KafkaTemplate<String, Race> kafkaTemplate;

    public RaceController(

            RaceRepository raceRepository,

            StarterRepository starterRepository,

            KafkaTemplate<String, Race> kafkaTemplate

    ) {

        this.raceRepository = raceRepository;

        this.starterRepository = starterRepository;

        this.kafkaTemplate = kafkaTemplate;

    }

    @GetMapping("/startRace/{raceId}")
    public void startRace(@PathVariable Long raceId) throws RaceNotFoundException {

        Optional<Race> race = raceRepository.findById(raceId);

        kafkaTemplate.send(MESSAGE_BUS_START_RACE_TOPIC, race.orElseThrow(() -> new RaceNotFoundException(raceId)));

    }

    @GetMapping("/endRace/{raceId}")
    public void endRace(@PathVariable Long raceId) throws RaceNotFoundException {

        Optional<Race> race = raceRepository.findById(raceId);

        kafkaTemplate.send(MESSAGE_BUS_END_RACE_TOPIC, race.orElseThrow(() -> new RaceNotFoundException(raceId)));

    }

    @PostMapping("/add-race")
    public Race addRace(@RequestBody Race race) throws RaceNameException {

        if (Strings.isEmpty(race.getName())) throw new RaceNameException(race.getName());

        Race savedRace = raceRepository.save(race);

        return savedRace;
    }

    @GetMapping("/get-race/{raceId}")
    public Race getRace(@PathVariable Long raceId) throws RaceNotFoundException {

        raceRepository.findById(raceId);

        return raceRepository.findById(raceId).orElseThrow(() -> new RaceNotFoundException(raceId));
    }

    @PostMapping("/{raceId}/add-starter")
    public Starter addStarter(@PathVariable Long raceId, @RequestBody Starter starter) throws RaceNotFoundException {

        Optional<Race> race = raceRepository.findById(raceId);

        starter.setRace(race.orElseThrow(() -> new RaceNotFoundException(raceId)));

        return starterRepository.save(starter);

    }

    @GetMapping("/get-starter/{starterId}")
    public Starter getStarter(@PathVariable Long starterId) throws StarterNotFoundException {

        Optional<Starter> optionalStarter = starterRepository.findById(starterId);

        return optionalStarter.orElseThrow(() -> new StarterNotFoundException(starterId));

    }

    @GetMapping("/remove-starter/{starterId}")
    public void removeStarter(@PathVariable Long starterId) throws RaceNotFoundException, StarterNotFoundException {

        Optional<Starter> optionalStarter = starterRepository.findById(starterId);

        Starter starter = optionalStarter.orElseThrow(() -> new StarterNotFoundException(starterId));

        /* */

        starterRepository.delete(starter);

    }

}