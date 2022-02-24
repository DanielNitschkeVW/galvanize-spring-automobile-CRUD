package de.volkswagen.f73.galvanize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutosController.class)
public class AutosControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutosService autosService;

    ObjectMapper mapper = new ObjectMapper();

    // Get: /api/autos Response 200 Ok
    @Test
    void getAuto_noParams_exists_returnsListOfAutos() throws Exception {
        // Given
        List<Automobile> autos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            autos.add(new Automobile(2000+i, "Volkswagen", "ID.3", "TestVIN" + i));
        }
        when(autosService.getAutos()).thenReturn(new AutosList(autos));

        // When
        mockMvc.perform(get("/api/autos"))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    // Get: /api/autos Response 204 No content
    @Test
    void getAutos_noParams_notExists_returnsEmptyListOfAutos() throws Exception {
        // Given
        when(autosService.getAutos()).thenReturn(new AutosList());

        // When
        mockMvc.perform(get("/api/autos/NotExistingVIN"))

                // Then
                .andExpect(status().isNoContent());
    }

    // Get: /api/autos/{vin} Response 204 No autos found by that vin
    @Test
    void getAutos_noParams_exists_returnsEmptyListOfAutos() throws Exception {
        // Given
        when(autosService.getAutos()).thenReturn(new AutosList());

        // When
        mockMvc.perform(get("/api/autos"))

                // Then
                .andExpect(status().isNoContent());
    }

    // Get: /api/autos?color={color} Response 200 successful operation
    @Test
    void getAutos_searchParamsColor_exists_returnsListOfAutos() throws Exception {
        // Given
        List<Automobile> autos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            autos.add(new Automobile(2000+i, "Volkswagen", "ID.3", "TestVIN" + i));
        }
        when(autosService.getAutosByColor(anyString())).thenReturn(new AutosList(autos));

        // When
        mockMvc.perform(get("/api/autos?color=RED"))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    // Get: /api/autos?color={color} Response 204 No autos found by that color
    @Test
    void getAutos_searchParamsColor_notExists_returns204() throws Exception {
        // Given
        doThrow(AutomobileColorNotFoundException.class)
            .when(autosService).getAutosByColor(anyString());

        // When
        mockMvc.perform(get("/api/autos?color=RED"))

                // Then
                .andExpect(status().isNoContent());
    }

    // Get: /api/autos?make={make} Response 200 successful operation
    @Test
    void getAutos_searchParamsMake_exists_returnsListOfAutos() throws Exception {
        // Given
        List<Automobile> autos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            autos.add(new Automobile(2000+i, "Volkswagen", "ID.3", "TestVIN" + i));
        }
        when(autosService.getAutosByMake(anyString())).thenReturn(new AutosList(autos));

        // When
        mockMvc.perform(get("/api/autos?make=Volkswagen"))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    // Get: /api/autos?make={make}?color={color} Response 200 successful operation
    @Test
    void getAutos_searchParams_exists_returnsListOfAutos() throws Exception {
        // Given
        List<Automobile> autos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            autos.add(new Automobile(2000+i, "Volkswagen", "ID.3", "TestVIN" + i));
        }
        when(autosService.getAutos(anyString(), anyString())).thenReturn(new AutosList(autos));

        // When
        mockMvc.perform(get("/api/autos?color=RED&make=Volkswagen"))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    // Post: /api/autos Response 200 Ok
    @Test
    void addAuto_valid_returnsAuto() throws Exception {
        // Given
        Automobile auto = new Automobile(1985, "Volkswagen", "Käfer", "VWWOBKäfer");
        when(autosService.addAuto(any(Automobile.class))).thenReturn(auto);

        // When
        mockMvc.perform(post("/api/autos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(auto)))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("make").value("Volkswagen"));
    }

    // Post: /api/autos Response 400 Error bad request
    @Test
    void addAuto_badRequest_throwInvalidAutomobileException() throws Exception {
        // Given
        when(autosService.addAuto(any(Automobile.class))).thenThrow(InvalidAutomobileException.class);

        // When
        mockMvc.perform(post("/api/autos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                // Then
                .andExpect(status().isBadRequest());
    }

    // Get: /api/autos/{vin} Response 200 successful operation
    @Test
    void getAuto_withVin_returnsAuto() throws Exception {
        // Given
        Automobile auto = new Automobile(1985, "Volkswagen", "Käfer", "VWWOBKäfer");
        when(autosService.getAuto(anyString())).thenReturn(auto);

        // When
        mockMvc.perform(get("/api/autos/" + auto.getVin()))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("vin").value(auto.getVin()));
    }

    // Get: /api/autos/{vin} Response 204 No autos found by that vin
    @Test
    void getAuto_withVin_notExists_returnNoContent() throws Exception {
        // Given
        doThrow(new AutomobileNotFoundException()).when(autosService).getAuto(anyString());

        // When
        mockMvc.perform(get("/api/autos/nonExistingVIN"))

        // Then
            .andExpect(status().isNoContent());
        verify(autosService).getAuto(anyString());
    }

    // Patch: /api/autos/{vin} Response 200 OK
    @Test
    void updateAuto_withObject_returnsAuto() throws Exception {
        // Given
        Automobile auto = new Automobile(1985, "Volkswagen", "Käfer", "VWWOBKäfer");
        auto.setColor("GREEN");
        auto.setOwner("me");
        when(autosService.updateAuto(anyString(),anyString(),anyString()))
        .thenReturn(auto);

        // When
        mockMvc.perform(patch("/api/autos/" + auto.getVin())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"color\": \"GREEN\", \"owner\": \"me\"}"))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("color").value(auto.getColor()))
                .andExpect(jsonPath("owner").value(auto.getOwner()));
    }

    // Delete: /api/autos/{vin} Response 202 delete request accepted
    @Test
    void deleteAuto_withVin_exists_return202() throws Exception {
        // Given

        // When
        mockMvc.perform(delete("/api/autos/ExistingVIN"))

                // Then
                .andExpect(status().isAccepted());
        verify(autosService).deleteAuto(anyString());
    }

    // Delete: /api/autos/{vin} Response 204 No autos found by that vin
    @Test
    void deleteAuto_withVin_notExists_returnNoContent() throws Exception {
        // Given
        doThrow(new AutomobileNotFoundException()).when(autosService).deleteAuto(anyString());

        // When
        mockMvc.perform(delete("/api/autos/nonExistingVIN"))

                // Then
                .andExpect(status().isNoContent());
        verify(autosService).deleteAuto(anyString());
    }

    /*
        Patch: /api/autos/{vin} Response 204 No autos found by that vin
        Patch: /api/autos/{vin} Response 400 Bad request (no payload, no change, already done)
     */
}
