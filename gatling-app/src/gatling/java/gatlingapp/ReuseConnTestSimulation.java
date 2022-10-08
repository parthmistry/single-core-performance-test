package gatlingapp;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ReuseConnTestSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://host2:8080");

    ScenarioBuilder scn = scenario("Reuse Connections").repeat(300).on(exec(http("demo")
            .get("/api/demo").requestTimeout(Duration.ofSeconds(30)).check(status().is(200)))
            .pause(Duration.ofSeconds(1)));

    {
        setUp(scn.injectOpen(atOnceUsers(1360)).protocols(httpProtocol));
    }

}
