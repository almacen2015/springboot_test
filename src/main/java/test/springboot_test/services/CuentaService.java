package test.springboot_test.services;

import java.math.BigDecimal;
import java.util.List;

import test.springboot_test.models.Cuenta;

public interface CuentaService {
    List<Cuenta> findAll();

    Cuenta findById(Long id);

    Cuenta save(Cuenta cuenta);

    int revisarTotalTransferencia(Long BancoId);

    BigDecimal revisarSaldo(Long CuentaId);

    void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long BancoId);

}
