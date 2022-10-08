package post.parthmistry.sc.demoweb.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class BackendController {

    private static final Logger logger = LoggerFactory.getLogger(BackendController.class);

    @PostMapping("/request-rate-throttle-check")
    public Mono<Map<String, Object>> requestThrottle(@RequestBody Map<String, Object> requestMap) {
        logger.info("requestThrottle() " + requestMap.get("requestId"));
        return Mono.just(Map.of(
                "requestId", requestMap.get("requestId"),
                "status", "SUCCESS"
        ));
    }

    @PostMapping("/track")
    public Mono<Map<String, Object>> track(@RequestBody Map<String, Object> requestMap) {
        logger.info("track() " + requestMap.get("requestId"));
        return Mono.just(Map.of(
                "requestId", requestMap.get("requestId"),
                "status", "SUCCESS"
        ));
    }

    @PostMapping("/authenticate")
    public Mono<Map<String, Object>> authenticate(@RequestBody Map<String, Object> requestMap) {
        logger.info("authenticate() " + requestMap.get("requestId"));
        return Mono.just(Map.of(
                "requestId", requestMap.get("requestId"),
                "status", "SUCCESS"
        ));
    }

    @PostMapping("/authorize")
    public Mono<Map<String, Object>> authorize(@RequestBody Map<String, Object> requestMap) {
        logger.info("authorize() " + requestMap.get("requestId"));
        return Mono.just(Map.of(
                "requestId", requestMap.get("requestId"),
                "status", "SUCCESS"
        ));
    }

    @GetMapping("/products")
    public Mono<Map<String, Object>> getProducts() {
        logger.info("getProducts()");
        return Mono.just(Map.of(
                "status", "SUCCESS"
        ));
    }

    @GetMapping("/products/{productId}")
    public Mono<Map<String, Object>> getProductDetail(@PathVariable Long productId) {
        logger.info("getProductDetail() " + productId);
        return Mono.just(Map.of(
                "productId", productId,
                "status", "SUCCESS"
        ));
    }

    @GetMapping("/products/{productId}/ratings")
    public Mono<Map<String, Object>> getRating(@PathVariable Long productId) {
        logger.info("getRating() " + productId);
        return Mono.just(Map.of(
                "productId", productId,
                "status", "SUCCESS"
        ));
    }

    @GetMapping("/products/{productId}/reviews")
    public Mono<Map<String, Object>> getReviews(@PathVariable Long productId) {
        logger.info("getReviews() " + productId);
        return Mono.just(Map.of(
                "productId", productId,
                "status", "SUCCESS"
        ));
    }

}
