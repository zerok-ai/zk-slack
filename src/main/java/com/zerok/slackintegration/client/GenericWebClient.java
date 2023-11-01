package com.zerok.slackintegration.client;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GenericWebClient {

    private final WebClient webClient;

    public GenericWebClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public <T> Mono<T> get(String uri, Class<T> responseType) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> post(String uri, Object request, Class<T> responseType) {
        return webClient.post()
                .uri(uri)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> put(String uri, Object request, Class<T> responseType) {
        return webClient.put()
                .uri(uri)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(responseType);
    }
}
