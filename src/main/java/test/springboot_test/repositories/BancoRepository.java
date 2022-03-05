package test.springboot_test.repositories;

import java.util.List;

import test.springboot_test.models.Banco;

public interface BancoRepository {
    Banco findById(Long id);

    List<Banco> findAll();

    void update(Banco banco);
}
