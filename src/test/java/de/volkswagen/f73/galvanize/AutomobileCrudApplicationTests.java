package de.volkswagen.f73.galvanize;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutomobileCrudApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AutosRepository autosRepository;

    Random rand = new Random();
    List<Automobile> autosList;
    final int NUM_AUTOMOBILES = 50;

    @BeforeEach
    void init() {
        this.autosList = new ArrayList<>();
        Automobile auto;

        final String[] COLORS = {"RED", "GREEN", "BLUE", "TEAL", "BLACK", "WHITE", "SILVER", "YELLOW", "ORANGE"};
        final String[] MAKES = {"Volkswagen", "Audi", "Porsche", "Bugatti", "Lamborghini", "Ducati", "MAN", "SEAT", "Skoda", "Bentley", "Scania"};
        final String[] MODEL = {"Cabrio", "Pickup", "Truck"};

        for (int i = 0; i < NUM_AUTOMOBILES; i++) {
            int r = rand.nextInt(300);

            int year = 1960 + rand.nextInt(63);
            String make = MAKES[r % MAKES.length];
            String model = MODEL[r % MODEL.length];
            String vin = year + "_" + make + "_" + model + "_" + rand.nextInt(100_000);
            String color = COLORS[r % COLORS.length];

            auto = new Automobile(year, make, model, vin);
            auto.setColor(color);

            this.autosList.add(auto);
        }
        autosRepository.saveAll(this.autosList);
    }

    @AfterEach
    void tearDown() {
        autosRepository.deleteAll();
    }

	@Test
	void contextLoads() {
	}

    @Test
    void getAutos_exits_returnsAutosList() {
        ResponseEntity<AutosList> response = restTemplate.getForEntity("/api/autos", AutosList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEmpty()).isFalse();
        assertThat(response.getBody().getAutomobiles().size()).isEqualTo(NUM_AUTOMOBILES);
        //response.getBody().getAutomobiles().forEach(System.out::println);
    }

    @Test
    void getAutos_searchByVin_returnsAuto() {
        int randomCarIndex = rand.nextInt(NUM_AUTOMOBILES);
        Automobile expectedCar = this.autosList.get(randomCarIndex);

        ResponseEntity<Automobile> response = restTemplate.getForEntity("/api/autos/" + expectedCar.getVin(), Automobile.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(expectedCar);
    }

    @Test
    void getAutos_searchByColorAndMake_returnsAutosList() {
        int randomCarIndex = rand.nextInt(NUM_AUTOMOBILES);
        Automobile primeAuto = this.autosList.get(randomCarIndex);

        List<Automobile> expectedAutos = this.autosList.stream()
                .filter(auto -> auto.getMake().equals(primeAuto.getMake()))
                .filter(auto -> auto.getColor().equals(primeAuto.getColor()))
                .collect(Collectors.toList());

        ResponseEntity<AutosList> response = restTemplate.getForEntity(
                String.format("/api/autos?color=%s&make=%s", primeAuto.getColor(), primeAuto.getMake()),
                AutosList.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEmpty()).isFalse();
        assertThat(response.getBody().getAutomobiles()).isNotNull();
        assertThat(response.getBody().getAutomobiles().size()).isEqualTo(expectedAutos.size());
    }
}
