package gatlingapp;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class WarmupSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://host2:8080");

    ScenarioBuilder scn = scenario("Warmup Server")
            .exec(http("warmup")
                    .get("/api/demo").requestTimeout(Duration.ofSeconds(30)).check(status().is(200)));

    {
        setUp(scn.injectOpen(constantUsersPerSec(120).during(Duration.ofMinutes(10))).protocols(httpProtocol));
    }

}
