package com.orcamento.orcamento_ai.repository;

import com.orcamento.orcamento_ai.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository
        extends JpaRepository<Transacao, Long> {
}