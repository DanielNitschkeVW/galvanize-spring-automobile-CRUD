package de.volkswagen.f73.galvanize;

import org.springframework.stereotype.Service;

@Service
public class AutosService {

    private AutosRepository autosRepository;

    public AutosService(AutosRepository autosRepository) {
        this.autosRepository = autosRepository;
    }

    public AutosList getAutos() {
        return new AutosList(autosRepository.findAll());
    }

    public AutosList getAutos(String color, String make) {
        return new AutosList(autosRepository.findByColorAndMake(color, make));
    }

    public AutosList getAutosByColor(String color) {
        return null;
    }

    public AutosList getAutosByMake(String make) {
        return null;
    }

    public Automobile addAuto(Automobile automobile) {
        return autosRepository.addAuto(automobile);
    }

    public Automobile getAuto(String vin) {
        return null;
    }

    public Automobile updateAuto(String vin, String color, String owner) {
        return null;
    }

    public void deleteAuto(String vin) {
    }

    public AutosList findByColor(String color) {
        return new AutosList(autosRepository.findByColor(color));
    }

    public AutosList findByMake(String make) {
        return new AutosList(autosRepository.findByMake(make));
    }
}
