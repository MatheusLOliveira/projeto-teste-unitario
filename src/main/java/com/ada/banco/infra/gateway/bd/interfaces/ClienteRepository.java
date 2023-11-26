package com.ada.banco.infra.gateway.bd.interfaces;

import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByCpf(String cpf);

}
