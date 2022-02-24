package de.volkswagen.f73.galvanize;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutosController {

    AutosService autosService;

    public AutosController(AutosService autosService) {
        this.autosService = autosService;
    }

    @GetMapping("/api/autos")
    public ResponseEntity<AutosList> getAutos() {
        AutosList autos = autosService.getAutos();
        return autos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(autos);
    }
}
