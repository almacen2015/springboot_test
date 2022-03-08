package test.springboot_test.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import test.springboot_test.models.TransaccionDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerWebTestClientTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testTransferir() {
        //GIVEN
        TransaccionDTO transaccionDTO = new TransaccionDTO();
        transaccionDTO.setCuentaOrigenId(1L);
        transaccionDTO.setCuentaDestinoId(2L);
        transaccionDTO.setMonto(new BigDecimal("500"));
        transaccionDTO.setBancoId(1L);

        //WHEN
        webTestClient.post().uri("http://localhost:8080/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transaccionDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.mensaje").isNotEmpty()
                .jsonPath("$.mensaje").value(is("transferencia realizada con exito"))
                .jsonPath("$.mensaje").value(valor -> {assertEquals("transferencia realizada con exito", valor);})
                .jsonPath("$.transaccion.cuentaOrigenId").isEqualTo(transaccionDTO.getCuentaOrigenId())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString());

        //THEN
    }
}