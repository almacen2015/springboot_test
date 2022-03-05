package test.springboot_test;

import java.math.BigDecimal;
import java.util.Optional;

import test.springboot_test.models.Banco;
import test.springboot_test.models.Cuenta;

public class Datos {
   /* public static final Cuenta CUENTA_001 = new Cuenta(1L, "VICTOR", new BigDecimal("5000"));
    public static final Cuenta CUENTA_002 = new Cuenta(1L, "VICTOR", new BigDecimal("2000"));
    public static final Banco BANCO = new Banco(1L, "BANCO FINANCIERO", 0);*/

    public static Optional<Cuenta> crearCuenta001(){
        return Optional.of(new Cuenta(1L, "VICTOR", new BigDecimal("5000")));
    }

    public static Optional<Cuenta> crearCuenta002(){
        return Optional.of(new Cuenta(2L, "ANDRES", new BigDecimal("2000")));
    }

    public static Optional<Banco> crearBanco(){
        return Optional.of(new Banco(1L, "BANCO FINANCIERO", 0));
    }
}
