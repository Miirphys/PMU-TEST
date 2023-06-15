import com.pmu.miirphys.race.repository.RaceRepository;
import com.pmu.miirphys.race.repository.StarterRepository;
import com.pmu.miirphys.race.repository.model.Race;
import com.pmu.miirphys.race.repository.model.Starter;
import com.pmu.miirphys.race.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @Author ponthiaux.eric@gmail.com
 */

class RaceControllerTest {

    @Mock
    private RaceRepository raceRepository;

    @Mock
    private StarterRepository starterRepository;

    @Mock
    private KafkaTemplate<String, Race> kafkaTemplate;

    @InjectMocks
    private RaceController raceController;

    private Random random = new Random();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void addRace_shouldSaveRace() throws RaceNameException {

        Race race = new Race();
        race.setName("Example Race");

        raceController.addRace(race);

        verify(raceRepository).save(race);
    }

    @Test
    void addRace_withEmptyRaceName_shouldThrowRaceNameException() {

        Race race = new Race();
        race.setName("");

        assertThrows(RaceNameException.class, () -> raceController.addRace(race));
        verify(raceRepository, never()).save(race);
    }

    @Test
    void getRace_withExistingRaceId_shouldReturnRace() throws RaceNotFoundException {

        Long raceId = random.nextLong();;
        Race race = new Race();
        race.setId(raceId);

        when(raceRepository.findById(raceId)).thenReturn(Optional.of(race));

        Race result = raceController.getRace(raceId);

        assertEquals(race, result);
    }

    @Test
    void getRace_withNonExistingRaceId_shouldThrowRaceNotFoundException() {

        Long raceId = random.nextLong();;

        when(raceRepository.findById(raceId)).thenReturn(Optional.empty());

        assertThrows(RaceNotFoundException.class, () -> raceController.getRace(raceId));
    }

    @Test
    void addStarter_withExistingRace_shouldSaveStarter() throws RaceNotFoundException {

        Long raceId = random.nextLong();
        Starter starter = new Starter();
        Race race = new Race();
        race.setId(raceId);

        when(raceRepository.findById(raceId)).thenReturn(Optional.of(race));

        raceController.addStarter(raceId, starter);

        verify(starterRepository).save(starter);
        assertEquals(race, starter.getRace());
    }

    @Test
    void addStarter_withNonExistingRaceId_shouldThrowRaceNotFoundException() {

        Long raceId = random.nextLong();
        Starter starter = new Starter();

        when(raceRepository.findById(raceId)).thenReturn(Optional.empty());

        assertThrows(RaceNotFoundException.class, () -> raceController.addStarter(raceId, starter));
        verify(starterRepository, never()).save(starter);
    }

    @Test
    void removeStarter_withExistingStarterId_shouldDeleteStarter() throws RaceNotFoundException, StarterNotFoundException {

        Long starterId = random.nextLong();
        Starter starter = new Starter();
        starter.setId(starterId);

        when(starterRepository.findById(starterId)).thenReturn(Optional.of(starter));

        raceController.removeStarter(starter.getId());

        verify(starterRepository).delete(starter);
    }

    @Test
    void removeStarter_withNonExistingStarterId_shouldThrowStarterNotFoundException() {

        Long starterId = random.nextLong();

        when(starterRepository.findById(starterId)).thenReturn(Optional.empty());

        assertThrows(StarterNotFoundException.class, () -> raceController.removeStarter(starterId));
        verify(starterRepository, never()).delete(any());
    }

    @Test
    void startRace_shouldSendStartRaceMessage() throws RaceNotFoundException {

        Long raceId = random.nextLong();;
        Race race = new Race();
        race.setId(raceId);

        when(raceRepository.findById(raceId)).thenReturn(Optional.of(race));

        raceController.startRace(raceId);

        verify(kafkaTemplate).send(RaceControllerConstants.MESSAGE_BUS_START_RACE_TOPIC, race);
    }

    @Test
    void endRace_shouldSendEndRaceMessage() throws RaceNotFoundException {

        Long raceId = random.nextLong();
        Race race = new Race();
        race.setId(raceId);

        when(raceRepository.findById(raceId)).thenReturn(Optional.of(race));

        raceController.endRace(raceId);

        verify(kafkaTemplate).send(RaceControllerConstants.MESSAGE_BUS_END_RACE_TOPIC, race);
    }


}