package test.springboot_test.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import test.springboot_test.models.Banco;
import test.springboot_test.models.Cuenta;
import test.springboot_test.repositories.BancoRepository;
import test.springboot_test.repositories.CuentaRepository;

@Service
public class CuentaServiceImpl implements CuentaService {

    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    public BigDecimal revisarSaldo(Long CuentaId) {
        Cuenta cuenta = cuentaRepository.findById(CuentaId).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    public int revisarTotalTransferencia(Long BancoId) {
        Banco banco = bancoRepository.findById(BancoId).orElseThrow();
        return banco.getTotalTransferencias();
    }

    @Override
    public void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long BancoId) {

        Cuenta cuentaOrigen = cuentaRepository.findById(numCuentaOrigen).orElseThrow();
        cuentaOrigen.debito(monto);
        cuentaRepository.save(cuentaOrigen);

        Cuenta cuentaDestino = cuentaRepository.findById(numCuentaDestino).orElseThrow();
        cuentaDestino.credito(monto);
        cuentaRepository.save(cuentaDestino);

        Banco banco = bancoRepository.findById(BancoId).orElseThrow();
        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepository.save(banco);
    }

}
