package de.volkswagen.f73.galvanize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutosServiceTest {

    AutosService autosService;

    @Mock
    AutosRepository autosRepository;

    @BeforeEach
    void init() {
        autosService = new AutosService(autosRepository);
    }

    @Test
    void getAutos_noArgs_findAll() {
        when(autosRepository.findAll()).thenReturn(List.of(new Automobile(2000, "Volkswagen", "ID.3", "TestVIN")));
        AutosList autosList = autosService.getAutos();
        assertThat(autosList).isNotNull();
        assertThat(autosList.isEmpty()).isFalse();
    }

    @Test
    void getAutos_findByColorAndMake() {
        when(autosRepository.findByColorAndMake(anyString(), anyString())).thenReturn(List.of(new Automobile(2000, "Volkswagen", "ID.3", "TestVIN")));
        AutosList autosList = autosService.getAutos(anyString(), anyString());
        assertThat(autosList).isNotNull();
        assertThat(autosList.isEmpty()).isFalse();
    }

    @Test
    void getAutosByColor() {
    }

    @Test
    void getAutosByMake() {
    }

    @Test
    void addAuto() {
    }

    @Test
    void getAuto() {
    }

    @Test
    void updateAuto() {
    }

    @Test
    void deleteAuto() {
    }
}