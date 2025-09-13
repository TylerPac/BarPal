package dev.tylerpac.barpal.ollama.dto;

import java.util.Map;

/** Basic request payload for Ollama /api/generate (or /v1/chat/completions like endpoints if adapted). */
public class OllamaCompletionRequest {
    private String model; // e.g. "llama3" (must be pulled on the Ollama host)
    private String prompt; // user prompt or system + user combined
    private Boolean stream = false; // default non-streaming for simplicity
    private Map<String, Object> options; // temperature, top_p, etc.

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public Boolean getStream() { return stream; }
    public void setStream(Boolean stream) { this.stream = stream; }
    public Map<String, Object> getOptions() { return options; }
    public void setOptions(Map<String, Object> options) { this.options = options; }
}
