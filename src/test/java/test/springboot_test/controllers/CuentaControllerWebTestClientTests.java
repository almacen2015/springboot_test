package test.springboot_test.controllers;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import test.springboot_test.models.Cuenta;
import test.springboot_test.models.TransaccionDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerWebTestClientTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    void testTransferir() {
        //GIVEN
        TransaccionDTO transaccionDTO = new TransaccionDTO();
        transaccionDTO.setCuentaOrigenId(1L);
        transaccionDTO.setCuentaDestinoId(2L);
        transaccionDTO.setMonto(new BigDecimal("500"));
        transaccionDTO.setBancoId(1L);

        //WHEN
        webTestClient.post().uri("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transaccionDTO)
                .exchange()
                //THEN
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.mensaje").isNotEmpty()
                .jsonPath("$.mensaje").value(is("transferencia realizada con exito"))
                .jsonPath("$.mensaje").value(valor -> assertEquals("transferencia realizada con exito", valor))
                .jsonPath("$.transaccion.cuentaOrigenId").isEqualTo(transaccionDTO.getCuentaOrigenId())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString());
    }

    @Test
    @Order(2)
    void testDetalle() {
        webTestClient.get().uri("/api/cuentas/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("JUAN")
                .jsonPath("$.saldo").isEqualTo(4500);
    }

    @Test
    @Order(3)
    void testDetalle2() {
        webTestClient.get().uri("/api/cuentas/2").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(respuesta -> {
                    Cuenta cuenta = respuesta.getResponseBody();
                    assertEquals("ANDRES", cuenta.getPersona());
                    assertEquals("2500.00", cuenta.getSaldo().toPlainString());
                });
    }

    @Test
    @Order(4)
    void testListar() {
        webTestClient.get().uri("api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].persona").isEqualTo("JUAN")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].saldo").isEqualTo(4500)
                .jsonPath("$[1].persona").isEqualTo("ANDRES")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].saldo").isEqualTo(2500)
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));

    }

    @Test
    @Order(5)
    void testListar2() {
        webTestClient.get().uri("api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .consumeWith(response -> {
                    List<Cuenta> cuentas = response.getResponseBody();
                    assertNotNull(cuentas);
                    assertEquals(2, cuentas.size());
                    assertEquals("JUAN", cuentas.get(0).getPersona());
                    assertEquals("ANDRES", cuentas.get(1).getPersona());
                    assertEquals("4500.0", cuentas.get(0).getSaldo().toPlainString());
                    assertEquals("2500.0", cuentas.get(1).getSaldo().toPlainString());
                    assertFalse(cuentas.isEmpty());
                });
    }

    @Test
    @Order(6)
    void testGuardar() {
        //given
        Cuenta cuenta = new Cuenta(null, "PEPE", new BigDecimal("3000"));
        //when
        webTestClient.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                //then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("PEPE")
                .jsonPath("$.saldo").value(is(3000))
                .jsonPath("$.id").isEqualTo(3);
    }

    @Test
    @Order(7)
    void testGuardar2() {
        //given
        Cuenta cuenta = new Cuenta(null, "PEPA", new BigDecimal("4000"));
        //when
        webTestClient.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                //then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                    Cuenta c = response.getResponseBody();
                    assertNotNull(c);
                    assertEquals("PEPA", c.getPersona());
                    assertEquals(4000, c.getSaldo().intValue());
                    assertEquals(4L, c.getId());
                });
    }

    @Test
    @Order(8)
    void testEliminar() {
        webTestClient.get().uri("/api/cuentas")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(4);

        webTestClient.delete().uri("/api/cuentas/1")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        webTestClient.get().uri("/api/cuentas")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(3);
    }
}