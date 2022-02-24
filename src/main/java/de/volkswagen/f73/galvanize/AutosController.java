package de.volkswagen.f73.galvanize;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
