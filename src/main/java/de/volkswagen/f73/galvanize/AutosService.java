package de.volkswagen.f73.galvanize;

import de.volkswagen.f73.galvanize.exceptions.AutomobileNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return new AutosList(autosRepository.findByColor(color));
    }

    public AutosList getAutosByMake(String make) {
        return new AutosList(autosRepository.findByMake(make));
    }

    public Automobile addAuto(Automobile automobile) {
        return autosRepository.addAuto(automobile);
    }

    public Automobile getAuto(String vin) {
        return autosRepository.findAutoByVin(vin).orElse(null);
    }

    public Automobile updateAuto(String vin, String color, String owner) {
        Optional<Automobile> optionalAuto = autosRepository.findAutoByVin(vin);
        if (optionalAuto.isPresent()) {
            Automobile auto = optionalAuto.get();
            auto.setColor(color);
            auto.setOwner(owner);
            return autosRepository.save(auto);
        }
        return null;
    }

    public void deleteAuto(String vin) {
        Optional<Automobile> optionalAuto = autosRepository.findAutoByVin(vin);
        if (optionalAuto.isPresent()) {
            autosRepository.delete(optionalAuto.get());
        } else {
            throw new AutomobileNotFoundException();
        }
    }
}
