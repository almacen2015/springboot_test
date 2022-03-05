package test.springboot_test;

import java.math.BigDecimal;

import test.springboot_test.models.Banco;
import test.springboot_test.models.Cuenta;

public class Datos {
   /* public static final Cuenta CUENTA_001 = new Cuenta(1L, "VICTOR", new BigDecimal("5000"));
    public static final Cuenta CUENTA_002 = new Cuenta(1L, "VICTOR", new BigDecimal("2000"));
    public static final Banco BANCO = new Banco(1L, "BANCO FINANCIERO", 0);*/

    public static Cuenta crearCuenta001(){
        return new Cuenta(1L, "VICTOR", new BigDecimal("5000")); 
    }

    public static Cuenta crearCuenta002(){
        return new Cuenta(1L, "VICTOR", new BigDecimal("2000")); 
    }

    public static Banco crearBanco(){
        return new Banco(1L, "BANCO FINANCIERO", 0);
    }
}
