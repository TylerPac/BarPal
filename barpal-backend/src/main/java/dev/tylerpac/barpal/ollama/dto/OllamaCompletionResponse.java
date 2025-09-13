package dev.tylerpac.barpal.ollama.dto;

/** Simplified subset of Ollama completion response. */
public class OllamaCompletionResponse {
    private String model;
    private String response; // aggregated text when stream=false
    private boolean done;

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
}
