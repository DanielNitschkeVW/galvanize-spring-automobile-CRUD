package de.volkswagen.f73.galvanize;

import de.volkswagen.f73.galvanize.exceptions.AutomobileColorNotFoundException;
import de.volkswagen.f73.galvanize.exceptions.AutomobileNotFoundException;
import de.volkswagen.f73.galvanize.exceptions.InvalidAutomobileException;
import de.volkswagen.f73.galvanize.exceptions.InvalidUpdateOwnerRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AutosController {

    AutosService autosService;

    public AutosController(AutosService autosService) {
        this.autosService = autosService;
    }

    @GetMapping("/api/autos")
    public ResponseEntity<AutosList> getAutos(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String make) {

        AutosList autos;
        boolean invalidColor = color == null || color.isEmpty();
        boolean invalidMake = make == null || make.isEmpty();

        if (invalidColor && invalidMake) {
            autos = autosService.getAutos();
        } else if (invalidColor) {
            autos = autosService.getAutosByMake(make);
        } else if (invalidMake) {
            autos = autosService.getAutosByColor(color);
        } else {
            autos = autosService.getAutos(color, make);
        }

        return autos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(autos);
    }

    @PostMapping("/api/autos")
    public Automobile addAuto(@RequestBody Automobile auto) {
        return autosService.addAuto(auto);
    }

    @GetMapping("/api/autos/{vin}")
    public ResponseEntity<Automobile> getAuto(@PathVariable String vin) {
        Automobile auto = autosService.getAuto(vin);
        return auto == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(auto);
    }

    @PatchMapping("/api/autos/{vin}")
    public ResponseEntity<Automobile> updateAuto(
            @PathVariable String vin,
            @RequestBody UpdateOwnerRequest update) {
        Automobile auto = autosService.updateAuto(vin, update.getColor(), update.getOwner());
        return auto == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(auto);
    }

    @DeleteMapping("/api/autos/{vin}")
    public ResponseEntity<Void> deleteAuto(@PathVariable String vin) {
        autosService.deleteAuto(vin);
        return ResponseEntity.accepted().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidAutoExceptionHandler(InvalidAutomobileException e) {}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidUpdateOwnerRequestHandler(InvalidUpdateOwnerRequestException e) {}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void notFoundAutoExceptionHandler(AutomobileNotFoundException e) {}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void notFoundAutoColorExceptionHandler(AutomobileColorNotFoundException e) {}
}
