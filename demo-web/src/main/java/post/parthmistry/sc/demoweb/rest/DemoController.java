package post.parthmistry.sc.demoweb.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import post.parthmistry.sc.demoweb.service.BackendService;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class DemoController {

    @Autowired
    private BackendService backendService;

    AtomicLong counter = new AtomicLong(0);

    @GetMapping("/demo")
    public Mono<Map<String, Object>> demoRequest() {
        var id = counter.incrementAndGet();
        return backendService.requestThrottle(id)
                .flatMap(throttleResponseMap -> backendService.track(id))
                .flatMap(trackResponseMap -> backendService.authenticate(id))
                .flatMap(authenticationResponseMap -> backendService.authorize(id))
                .flatMap(authorizeResponseMap -> backendService.getProducts())
                .flatMap(authorizeResponseMap -> backendService.getProductDetail(id))
                .flatMap(authorizeResponseMap -> backendService.getRatings(id))
                .flatMap(authorizeResponseMap -> backendService.getReviews(id));
    }

}
