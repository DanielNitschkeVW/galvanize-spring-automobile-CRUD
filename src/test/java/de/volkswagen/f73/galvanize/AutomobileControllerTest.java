package de.volkswagen.f73.galvanize;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

public class AutomobileControllerTest {
    /*
        Post: /api/autos Response 200 Ok
        Post: /api/autos Response 400 Error bad request
        Get: /api/autos Response 200 Ok
        Get: /api/autos Response 204 No autos found by that vin

        Get: /api/autos?color={color} Response 200 Ok successful operation
        Get: /api/autos?color={color} Response 204 No autos found by that vin

        Get: /api/autos?make={make} Response 200 successful operation
        Get: /api/autos?make={make} Response 204 No autos found by that vin

        Get: /api/autos?make={make}?color={color} Response 200 successful operation
        Get: /api/autos?make={make}?color={color} Response 204 No autos found by that vin

        Get: /api/autos/{vin} Response 200 successful operation
        Get: /api/autos/{vin} Response 204 No autos found by that vin
        Patch: /api/autos/{vin} Response 200 OK
        Patch: /api/autos/{vin} Response 204 No autos found by that vin
        Patch: /api/autos/{vin} Response 400 Bad request (no payload, no change, already done)
        Delete: /api/autos/{vin} Response 200 OK
        Delete: /api/autos/{vin} Response 202 Accepted
        Delete: /api/autos/{vin} Response 204 No autos found by that vin
     */
}
