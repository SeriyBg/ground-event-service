package com.explorer.groundevent;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin
public class GroundEventsController {

    private static final String USER_AGENT = "user-agent";
    private static final String USER = "user";
    private static final String[] TRACE_HEADERS = new String[] {"x-request-id", "x-b3-traceid", "x-b3-spanid", "x-b3-parentspanid", "x-b3-sampled", "x-b3-flags", "x-ot-span-context"};

    private final GroundEventService groundEventService;

    @GetMapping("/")
    public String health() {
        return "Ok";
    }

    @GetMapping("/groundevent/{count}")
    public List<GroundEvent> groundEvents(@PathVariable Integer count, HttpServletRequest request) {
        return groundEventService.getGroundEvents(count, getForwardHeaders(request));
    }

    private HttpHeaders getForwardHeaders(HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(USER_AGENT, request.getHeader(USER_AGENT));
        httpHeaders.add(USER, request.getHeader(USER));
        for (String traceHeader : TRACE_HEADERS) {
            String header = request.getHeader(traceHeader);
            if (StringUtils.isEmpty(header)) {
                continue;
            }
            log.info("Header - " + traceHeader + ": " + header);
            httpHeaders.add(traceHeader, header);
        }
        return httpHeaders;
    }
}
