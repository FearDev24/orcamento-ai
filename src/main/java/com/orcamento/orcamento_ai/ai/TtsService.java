package com.orcamento.orcamento_ai.ai;

import org.springframework.stereotype.Service;

import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.InputStreamReader;
@Service
public class TtsService {

    public Path gerarAudio(String texto) throws Exception {

        Path projeto = Path.of(System.getProperty("user.dir"));

        if (!Files.exists(projeto.resolve("tools"))) {
            projeto = projeto.resolve("orcamento-ai");
        }
        Path pastaPiper = projeto.resolve("tools/piper");

        String piper = pastaPiper
                .resolve("piper.exe")
                .toString();

        String modelo = pastaPiper
                .resolve("voices/pt_BR-faber-medium.onnx")
                .toString();

        String config = pastaPiper
                .resolve("voices/pt_BR-faber-medium.onnx.json")
                .toString();

        Path saida = Files.createTempFile(
                pastaPiper,
                "resposta-",
                ".wav"
        );

        ProcessBuilder pb = new ProcessBuilder(
                piper,
                "--model",
                modelo,
                "--config",
                config,
                "--output_file",
                saida.toString()
        );

        // IMPORTANTE:
        // Piper procura DLLs/espeak-ng-data nesta pasta
        pb.directory(pastaPiper.toFile());

        pb.redirectErrorStream(true);

        Process processo = pb.start();

        try (OutputStreamWriter writer = new OutputStreamWriter(
                processo.getOutputStream(),
                StandardCharsets.UTF_8
        )) {
            writer.write(texto);
            writer.write(System.lineSeparator());

        }
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(processo.getInputStream())
        )) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                System.out.println("PIPER: " + linha);
            }
        }
        int exitCode = processo.waitFor();
        System.out.println("EXIT CODE PIPER = " + exitCode);
        System.out.println("ARQUIVO GERADO = " + saida);
        System.out.println("TAMANHO WAV = " + Files.size(saida));
        if (exitCode != 0) {
            throw new RuntimeException(
                    "Piper terminou com erro: " + exitCode
            );
        }

        return saida;
    }
}