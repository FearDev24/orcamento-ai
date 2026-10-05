package com.orcamento.orcamento_ai.service;

import com.orcamento.orcamento_ai.model.Transacao;
import com.orcamento.orcamento_ai.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import com.orcamento.orcamento_ai.model.TipoTransacao;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository repository;

    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    public Transacao salvar(Transacao transacao) {
        return repository.save(transacao);
    }

    public List<Transacao> listarTodas() {
        return repository.findAll();
    }

    public BigDecimal calcularSaldo() {
        return repository.findAll()
                    .stream()
                    .map(transacao -> {
                        if (transacao.getTipo() == TipoTransacao.RECEITA) {
                            return transacao.getValor();
                        }

                        return transacao.getValor().negate();
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }



