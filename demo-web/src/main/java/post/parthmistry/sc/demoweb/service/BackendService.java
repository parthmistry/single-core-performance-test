package post.parthmistry.sc.demoweb.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.Map;

@Service
public class BackendService {

    private final WebClient webClient;

    public BackendService() {
        var provider = ConnectionProvider.builder("test")
                .maxConnections(10000)
                .pendingAcquireMaxCount(10000)
                .maxIdleTime(Duration.ofSeconds(55))
                .build();

        this.webClient = WebClient.builder()
                .baseUrl("http://host3:8080")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create(provider)))
                .build();
    }

    public Mono<Map<String, Object>> requestThrottle(long requestId) {
        return postRequest(requestId, "/api/request-rate-throttle-check");
    }

    public Mono<Map<String, Object>> track(long requestId) {
        return postRequest(requestId, "/api/track");
    }

    public Mono<Map<String, Object>> authenticate(long requestId) {
        return postRequest(requestId, "/api/authenticate");
    }

    public Mono<Map<String, Object>> authorize(long requestId) {
        return postRequest(requestId, "/api/authorize");
    }

    public Mono<Map<String, Object>> getProducts() {
        return getRequest("/api/products");
    }

    public Mono<Map<String, Object>> getProductDetail(long productId) {
        return getRequest("/api/products/" + productId);
    }

    public Mono<Map<String, Object>> getRatings(long productId) {
        return getRequest("/api/products/" + productId + "/ratings");
    }

    public Mono<Map<String, Object>> getReviews(long productId) {
        return getRequest("/api/products/" + productId + "/reviews");
    }

    private Mono<Map<String, Object>> postRequest(long requestId, String uri) {
        return webClient.post()
                .uri(uri)
                .header("Content-Type", "application/json")
                .bodyValue(Map.of("requestId", requestId))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(new ParameterizedTypeReference<>() {}));
    }

    private Mono<Map<String, Object>> getRequest(String uri) {
        return webClient.get()
                .uri(uri)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(new ParameterizedTypeReference<>() {}));
    }

}
