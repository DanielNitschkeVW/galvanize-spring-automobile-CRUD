package de.volkswagen.f73.galvanize;

import org.springframework.stereotype.Service;

@Service
public class AutosService {
    public AutosList getAutos() {
        return null;
    }

    public AutosList getAutos(String color, String make) {
        return null;
    }

    public AutosList getAutosByColor(String color) {
        return null;
    }

    public AutosList getAutosByMake(String make) {
        return null;
    }

    public Automobile addAuto(Automobile automobile) {
        return null;
    }

    public Automobile getAuto(String vin) {
        return null;
    }

    public Automobile updateAuto(String vin, String color, String owner) {
        return null;
    }
}
