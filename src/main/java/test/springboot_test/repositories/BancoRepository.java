package test.springboot_test.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import test.springboot_test.models.Banco;

public interface BancoRepository extends JpaRepository<Banco, Long> {
 //   Banco findById(Long id);

   // List<Banco> findAll();

    //void update(Banco banco);
}
