package dev.tylerpac.barpal.ollama;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.tylerpac.barpal.ollama.dto.OllamaCompletionRequest;
import dev.tylerpac.barpal.ollama.dto.OllamaCompletionResponse;

@RestController
@RequestMapping("/api/ollama")
public class OllamaController {

    private final OllamaClient client;

    @Autowired
    public OllamaController(OllamaClient client) {
        this.client = client;
    }

    @PostMapping("/generate")
    public OllamaCompletionResponse generate(@RequestBody OllamaCompletionRequest req) {
        if (req.getModel() == null || req.getModel().isBlank()) {
            req.setModel("llama3.1:8b"); // default model name (ensure it's pulled)
        }
        return client.generate(req);
    }
}
