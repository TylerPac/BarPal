package dev.tylerpac.barpal.ollama;

import dev.tylerpac.barpal.ollama.dto.OllamaCompletionRequest;
import dev.tylerpac.barpal.ollama.dto.OllamaCompletionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/** Simple blocking client for Ollama's /api/generate endpoint. */
@Component
public class OllamaClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final OllamaProperties props;

    @Autowired
    public OllamaClient(OllamaProperties props) {
        this.props = props;
    }

    public OllamaCompletionResponse generate(OllamaCompletionRequest request) {
        String url = props.getBaseUrl().replaceAll("/$", "") + "/api/generate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OllamaCompletionRequest> entity = new HttpEntity<>(request, headers);
        try {
            return restTemplate.postForObject(url, entity, OllamaCompletionResponse.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed calling Ollama at " + url + ": " + e.getMessage(), e);
        }
    }
}
