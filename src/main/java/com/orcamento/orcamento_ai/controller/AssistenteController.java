package com.orcamento.orcamento_ai.controller;

import com.orcamento.orcamento_ai.ai.AssistenteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assistente")
public class AssistenteController {

    private final AssistenteService assistenteService;

    public AssistenteController(AssistenteService assistenteService) {
        this.assistenteService = assistenteService;
    }

    @GetMapping
    public String perguntar(@RequestParam String mensagem) {
        return assistenteService.perguntar(mensagem);
    }
}