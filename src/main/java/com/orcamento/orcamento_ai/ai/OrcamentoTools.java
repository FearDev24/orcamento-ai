package com.orcamento.orcamento_ai.ai;
import com.orcamento.orcamento_ai.model.TipoTransacao;
import com.orcamento.orcamento_ai.model.Transacao;
import com.orcamento.orcamento_ai.model.CategoriaTransacao;
import java.time.LocalDate;
import com.orcamento.orcamento_ai.service.TransacaoService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrcamentoTools {

    private final TransacaoService transacaoService;

    public OrcamentoTools(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }
    private LocalDate resolverData(String data) {

        if (data == null || data.isBlank() || data.equalsIgnoreCase("hoje")) {
            return LocalDate.now();
        }

        if (data.equalsIgnoreCase("ontem")) {
            return LocalDate.now().minusDays(1);
        }

        return LocalDate.parse(data);
    }

    @Tool(description = "Registra uma transação financeira. Para data, use apenas HOJE ou ONTEM.")
    public String registrarTransacao(
            String descricao,
            BigDecimal valor,
            TipoTransacao tipo,
            CategoriaTransacao categoria,
            String referenciaData
    ) {
        Transacao transacao = new Transacao();

        transacao.setDescricao(descricao);
        transacao.setValor(valor);
        transacao.setTipo(tipo);
        transacao.setCategoria(categoria);

        if ("ONTEM".equalsIgnoreCase(referenciaData)) {
            transacao.setData(LocalDate.now().minusDays(1));
        } else {
            transacao.setData(LocalDate.now());
        }

        transacaoService.salvar(transacao);

        return "Transação registrada com sucesso";
    }
}