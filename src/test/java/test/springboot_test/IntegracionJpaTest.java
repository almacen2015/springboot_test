package test.springboot_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import test.springboot_test.models.Cuenta;
import test.springboot_test.repositories.CuentaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegracionJpaTest {

    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("JUAN", cuenta.orElseThrow().getPersona());

    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("ANDRES");
        assertTrue(cuenta.isPresent());
        assertEquals("ANDRES", cuenta.orElseThrow().getPersona());
        assertEquals("2000.00", cuenta.orElseThrow().getSaldo().toPlainString());

    }

    @Test
    void testFindByPersonaThrowException() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Rod");
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void testSave() {
        //GIVEN
        Cuenta cuentaPepe = new Cuenta(null, "PEPE", new BigDecimal("3000"));

        //WHEN
        Cuenta save = cuentaRepository.save(cuentaPepe);
        //Cuenta optionalCuenta = cuentaRepository.findByPersona("PEPE").orElseThrow();
        //Cuenta optionalCuenta = cuentaRepository.findById(save.getId()).orElseThrow();

        //THEN
        assertEquals("PEPE", save.getPersona());
        assertEquals("3000", save.getSaldo().toPlainString());

    }

    @Test
    void testUpdate() {
        //GIVEN
        Cuenta cuentaPepe = new Cuenta(null, "PEPE", new BigDecimal("3000"));

        //WHEN
        Cuenta save = cuentaRepository.save(cuentaPepe);
        //Cuenta optionalCuenta = cuentaRepository.findByPersona("PEPE").orElseThrow();
        //Cuenta optionalCuenta = cuentaRepository.findById(save.getId()).orElseThrow();

        //THEN
        assertEquals("PEPE", save.getPersona());
        assertEquals("3000", save.getSaldo().toPlainString());

        save.setSaldo(new BigDecimal("1800"));
        Cuenta cuentaUpdate = cuentaRepository.save(save);
        assertEquals("1800", cuentaUpdate.getSaldo().toPlainString());
    }

    @Test
    void testDelete() {
        Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow();
        assertEquals("ANDRES", cuenta.getPersona());
        cuentaRepository.delete(cuenta);
        assertThrows(NoSuchElementException.class, () -> {
            cuentaRepository.findById(2L).orElseThrow();
        });
        assertEquals(1, cuentaRepository.findAll().size());
    }
}
