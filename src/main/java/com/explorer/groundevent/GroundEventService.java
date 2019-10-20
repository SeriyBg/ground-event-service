package com.explorer.groundevent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GroundEventService {

    @Value("${events.url}")
    private String eventServiceUrl;
    @Value("${players.url}")
    private String playerServiceUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public GroundEventService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    List<GroundEvent> getGroundEvents(Integer count, HttpHeaders headers) {
        var playerScore = getPlayerScore(headers);
        List<GroundEvent> groundEvents = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            groundEvents.add(getGroundEvent(headers));
        }
        return groundEvents.stream()
                .filter(Objects::nonNull)
                .map(e -> e.enrich(playerScore))
                .collect(Collectors.toList());
    }

    @Nullable
    private GroundEvent getGroundEvent(HttpHeaders headers) {
        var url = eventServiceUrl + "/event/ground";
        log.info("eventServiceUrl: {}", url);
        try {
            ResponseEntity<GroundEvent> exchange =
                    restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>("", headers), GroundEvent.class);
            return exchange.getBody();
        } catch(Exception e) {
            log.error("Error on ground event request", e);
            return null;
        }
    }

    private Long getPlayerScore(HttpHeaders headers) {
        var url = playerServiceUrl + "/player/score";
        log.info("playerServiceUrl: {}", url);
        try {
            ResponseEntity<Long> exchange =
                    restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>("", headers), Long.class);
            return exchange.getBody();
        } catch(Exception e) {
            log.error("Error on player score request", e);
            return 0L;
        }
    }
}
