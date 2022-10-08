package gatlingapp;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class NewConnTestSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://host2:8080");

    ScenarioBuilder scn = scenario("New Connections")
            .exec(http("demo")
                    .get("/api/demo").requestTimeout(Duration.ofSeconds(30)).check(status().is(200)));

    {
        setUp(scn.injectOpen(constantUsersPerSec(940).during(300)).protocols(httpProtocol));
    }

}
