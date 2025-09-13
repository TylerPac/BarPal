package dev.tylerpac.barpal.status;

import dev.tylerpac.barpal.ollama.OllamaClient;
import dev.tylerpac.barpal.ollama.dto.OllamaCompletionRequest;
import dev.tylerpac.barpal.ollama.dto.OllamaCompletionResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final DbStatusRepository dbRepo;
    private final OllamaClient ollamaClient;

    public StatusController(DbStatusRepository dbRepo, OllamaClient ollamaClient) {
        this.dbRepo = dbRepo;
        this.ollamaClient = ollamaClient;
    }

    @GetMapping("/db")
    public Map<String, Object> dbStatus() {
        Map<String, Object> result = new HashMap<>();
        try {
            // write then read back quickly (lightweight)
            StatusProbeEntity saved = dbRepo.save(new StatusProbeEntity("probe"));
            result.put("status", "UP");
            result.put("insertedId", saved.getId());
            result.put("count", dbRepo.count());
        } catch (DataAccessException e) {
            result.put("status", "DOWN");
            result.put("error", e.getMessage());
        }
        return result;
    }

    @PostMapping("/ollama-test")
    public Map<String, Object> testOllama(@RequestBody(required = false) Map<String, Object> body) {
        String prompt = body != null && body.get("prompt") != null ? body.get("prompt").toString() : "Say hello";
        OllamaCompletionRequest req = new OllamaCompletionRequest();
        req.setModel("llama3");
        req.setPrompt(prompt);
        OllamaCompletionResponse resp = ollamaClient.generate(req);
        Map<String, Object> out = new HashMap<>();
        out.put("status", resp != null && resp.isDone() ? "UP" : "UNKNOWN");
        out.put("model", resp != null ? resp.getModel() : null);
        out.put("responseSample", resp != null ? resp.getResponse() : null);
        return out;
    }
}
