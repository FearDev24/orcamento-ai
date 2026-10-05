package com.orcamento.orcamento_ai.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AssistenteService {

    private final ChatClient chatClient;
    private final OrcamentoTools orcamentoTools;



    public AssistenteService(
            ChatClient.Builder builder,
            OrcamentoTools orcamentoTools
    ) {
        this.chatClient = builder.build();
        this.orcamentoTools = orcamentoTools;
    }

    public String perguntar(String mensagem) {
        return chatClient
                .prompt()
                .user(mensagem)
                .tools(orcamentoTools)
                .call()
                .content();
    }


}