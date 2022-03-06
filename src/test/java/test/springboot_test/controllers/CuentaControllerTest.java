package test.springboot_test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import test.springboot_test.Datos;
import test.springboot_test.models.Cuenta;
import test.springboot_test.models.TransaccionDTO;
import test.springboot_test.services.CuentaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CuentaService service;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void detalleDetalle() throws Exception {
        //GIVEN
        when(service.findById(1L)).thenReturn(Datos.crearCuenta001().orElseThrow());

        //WHEN
        mockMvc.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isOk()) // que el status esperado sea OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // que la respuesta sea un JSON
                .andExpect(jsonPath("$.persona").value("VICTOR"))
                .andExpect(jsonPath("$.saldo").value("5000"));

        verify(service).findById(1L);

    }

    @Test
    void testTransferir() throws Exception {
        //GIVEN
        TransaccionDTO dto = new TransaccionDTO();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setMonto(new BigDecimal("500"));
        dto.setBancoId(1L);

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "ok");
        response.put("mensaje", "transferencia realizada con exito");
        response.put("transaccion", dto);
        System.out.println(objectMapper.writeValueAsString(response));

        //WHEN
        mockMvc.perform(post("/api/cuentas/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))

                //THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value("transferencia realizada con exito"))
                .andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(dto.getCuentaOrigenId()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testListar() throws Exception {
        //GIVEN
        List<Cuenta> cuentas = Arrays.asList(Datos.crearCuenta001().orElseThrow(), Datos.crearCuenta002().orElseThrow());
        when(service.findAll()).thenReturn(cuentas);

        //WHEN
        mockMvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].persona").value("VICTOR"))
                .andExpect(jsonPath("$[1].persona").value("ANDRES"))
                .andExpect(jsonPath("$[0].saldo").value("5000"))
                .andExpect(jsonPath("$[1].saldo").value("2000"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(cuentas)));
    }

    @Test
    void testGuardar() throws Exception {
        //GIVEN
        Cuenta cuenta = new Cuenta(null, "PEPE", new BigDecimal("3000"));
        when(service.save(any())).then(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        //when
        mockMvc.perform(post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuenta)))
                //THEN
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("PEPE"))
                .andExpect(jsonPath("$.saldo").value(3000));

    }
}