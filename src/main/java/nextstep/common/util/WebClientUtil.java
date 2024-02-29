package nextstep.common.util;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
public class WebClientUtil {

    private final WebClient webClient;


    public WebClientUtil(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> Mono<T> get(String url, Class<T> responseType) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> get(String url, MultiValueMap<String, String> headers, Class<T> responseType) {
        return webClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> getWithParams(String url, MultiValueMap<String, String> params, Class<T> responseType) {
        return webClient.get()
                .uri(UriComponentsBuilder.fromHttpUrl(url)
                        .queryParams(params).build().toUri())
                .retrieve()
                .bodyToMono(responseType);

    }

    public <T> Mono<T> post(String url, Object requestBody, MediaType mediaType, Class<T> responseType) {
        return webClient.post()
                .uri(url)
                .contentType(mediaType)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }
}
