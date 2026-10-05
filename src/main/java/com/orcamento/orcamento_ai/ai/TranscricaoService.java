package com.orcamento.orcamento_ai.ai;
import java.nio.file.Files;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;

@Service
public class TranscricaoService {

    public String transcrever(String caminhoAudio) throws Exception {

        Path projeto = Path.of(System.getProperty("user.dir"));

        if (!Files.exists(projeto.resolve("tools"))) {
            projeto = projeto.resolve("orcamento-ai");
        }
        String whisper = projeto
                .resolve("tools/whisper/whisper-cli.exe")
                .toString();

        String modelo = projeto
                .resolve("tools/whisper/models/ggml-base.bin")
                .toString();

        ProcessBuilder processBuilder = new ProcessBuilder(
                whisper,
                "-m",
                modelo,
                "-f",
                caminhoAudio,
                "-l",
                "pt",
                "-nt"
        );

        Process processo = processBuilder.start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(processo.getInputStream())
        );

        StringBuilder texto = new StringBuilder();

        String linha;

        while ((linha = reader.readLine()) != null) {
            texto.append(linha);
        }

        processo.waitFor();

        return texto.toString().trim();
    }
}