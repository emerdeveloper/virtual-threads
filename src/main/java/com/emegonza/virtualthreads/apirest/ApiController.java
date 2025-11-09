package com.emegonza.virtualthreads.apirest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping(value = "/api", produces = MediaType.TEXT_PLAIN_VALUE)
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    private final RestClient restClient;

    public ApiController(RestClient.Builder restClientBuilder) {
        restClient = restClientBuilder.baseUrl("https://postman-echo.com/").build();
    }

    @GetMapping(path = "/simulate-calls/{seconds}")
    public String simulateCalls(@PathVariable int seconds) {
        ResponseEntity<Void> result = restClient.get()
                .uri("/delay/" + seconds)
                .retrieve()
                .toBodilessEntity();

        log.info("{} on {}", result.getStatusCode(), Thread.currentThread());

        return Thread.currentThread().toString();
    }
}
