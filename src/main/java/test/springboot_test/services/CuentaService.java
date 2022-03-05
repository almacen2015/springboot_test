package test.springboot_test.services;

import java.math.BigDecimal;

import test.springboot_test.models.Cuenta;

public interface CuentaService {
    Cuenta findById(Long id);

    int revisarTotalTransferencia(Long BancoId);

    BigDecimal revisarSaldo(Long CuentaId);

    void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long BancoId);

}
