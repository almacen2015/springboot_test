package test.springboot_test.repositories;

import java.util.List;

import test.springboot_test.models.Cuenta;

public interface CuentaRepository {
    List<Cuenta> findAll();

    Cuenta findById(Long id);

    void update(Cuenta cuenta);
}
