package com.orcamento.orcamento_ai.controller;

import com.orcamento.orcamento_ai.model.Transacao;
import com.orcamento.orcamento_ai.service.TransacaoService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping
    public Transacao criar(@RequestBody Transacao transacao) {
        return service.salvar(transacao);
    }

    @GetMapping
    public List<Transacao> listar() {
        return service.listarTodas();
    }

    @GetMapping("/saldo")
    public BigDecimal saldo() {
        return service.calcularSaldo();
    }

}