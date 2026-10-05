package com.orcamento.orcamento_ai.controller;
import com.orcamento.orcamento_ai.ai.TtsService;
import com.orcamento.orcamento_ai.ai.AssistenteService;
import com.orcamento.orcamento_ai.ai.TranscricaoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.nio.file.Files;
import java.nio.file.Path;
import com.orcamento.orcamento_ai.ai.AudioConversaoService;
@RestController
@RequestMapping("/audio")
public class AudioController {
    private final TtsService ttsService;
    private final TranscricaoService transcricaoService;
    private final AssistenteService assistenteService;
    private final AudioConversaoService audioConversaoService;
    public AudioController(
            TranscricaoService transcricaoService,
            AssistenteService assistenteService,
            TtsService ttsService,
            AudioConversaoService audioConversaoService

    ) {
        this.transcricaoService = transcricaoService;
        this.assistenteService = assistenteService;
        this.ttsService = ttsService;
        this.audioConversaoService = audioConversaoService;
    }

    @PostMapping(produces = "audio/wav")
    public ResponseEntity<byte[]> processarAudio(
            @RequestParam("arquivo") MultipartFile arquivo
    ) throws Exception {

        Path original = Files.createTempFile("audio-original-", ".tmp");

        arquivo.transferTo(original);

        Path wav = audioConversaoService.converterParaWav(original);

        String texto = transcricaoService.transcrever(
                wav.toAbsolutePath().toString()
        );


        if (texto == null || texto.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body("Não foi possível transcrever o áudio.".getBytes());
        }


        String resposta = assistenteService.perguntar(texto);



        Path audioResposta = ttsService.gerarAudio(resposta);

        byte[] bytes = Files.readAllBytes(audioResposta);

        Files.deleteIfExists(original);
        Files.deleteIfExists(wav);
        Files.deleteIfExists(audioResposta);

        return ResponseEntity.ok()
                .header("Content-Type", "audio/wav")
                .body(bytes);

}

}