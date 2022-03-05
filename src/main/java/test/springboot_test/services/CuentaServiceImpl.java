package test.springboot_test.services;

import java.math.BigDecimal;

import test.springboot_test.models.Banco;
import test.springboot_test.models.Cuenta;
import test.springboot_test.repositories.BancoRepository;
import test.springboot_test.repositories.CuentaRepository;

public class CuentaServiceImpl implements CuentaService {

    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id);
    }

    @Override
    public BigDecimal revisarSaldo(Long CuentaId) {
        Cuenta cuenta = cuentaRepository.findById(CuentaId);
        return cuenta.getSaldo();
    }

    @Override
    public int revisarTotalTransferencia(Long BancoId) {
        Banco banco = bancoRepository.findById(BancoId);
        return banco.getTotalTransferencias();
    }

    @Override
    public void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long BancoId) {

        Cuenta cuentaOrigen = cuentaRepository.findById(numCuentaOrigen);
        cuentaOrigen.debito(monto);
        cuentaRepository.update(cuentaOrigen);

        Cuenta cuentaDestino = cuentaRepository.findById(numCuentaDestino);
        cuentaDestino.credito(monto);
        cuentaRepository.update(cuentaDestino);

        Banco banco = bancoRepository.findById(BancoId);
        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepository.update(banco);
    }

}
