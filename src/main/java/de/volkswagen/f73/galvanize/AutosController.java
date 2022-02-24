package de.volkswagen.f73.galvanize;

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

        return autos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(autos);
    }

    @PostMapping("/api/autos")
    public Automobile addAuto(@RequestBody Automobile auto) {
        return autosService.addAuto(auto);
    }

    @GetMapping("/api/autos/{vin}")
    public Automobile getAuto(@PathVariable String vin) {
        return autosService.getAuto(vin);
    }

    @PatchMapping("/api/autos/{vin}")
    public Automobile updateAuto(
            @PathVariable String vin,
            @RequestBody UpdateOwnerRequest updateRequest) {
        return autosService.updateAuto(vin, updateRequest.getColor(), updateRequest.getOwner());
    }

    @DeleteMapping("/api/autos/{vin}")
    public ResponseEntity deleteAuto(@PathVariable String vin) {
        autosService.deleteAuto(vin);
        return ResponseEntity.accepted().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidAutoExceptionHandler(InvalidAutomobileException e) {}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void notFoundAutoExceptionHandler(AutomobileNotFoundException e) {}
}
