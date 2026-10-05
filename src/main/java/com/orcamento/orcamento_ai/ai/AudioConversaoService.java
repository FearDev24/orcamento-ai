package com.orcamento.orcamento_ai.ai;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AudioConversaoService {

    public Path converterParaWav(Path entrada) throws Exception {

        Path projeto = Path.of(System.getProperty("user.dir"));

        if (!Files.exists(projeto.resolve("tools"))) {
            projeto = projeto.resolve("orcamento-ai");
        }


        String ffmpeg = projeto
                .resolve("tools/ffmpeg/bin/ffmpeg.exe")
                .toString();

        Path saida = Files.createTempFile("audio-convertido-", ".wav");

        ProcessBuilder pb = new ProcessBuilder(
                ffmpeg,
                "-y",
                "-i",
                entrada.toString(),
                "-ar",
                "16000",
                "-ac",
                "1",
                saida.toString()
        );

        pb.redirectErrorStream(true);
       ;
        Process processo = pb.start();

        int exitCode = processo.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Erro ao converter áudio com FFmpeg");
        }

        return saida;
    }
}