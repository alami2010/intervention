package com.ydahar.jbd.web.rest;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloResource {

    private final Logger log = LoggerFactory.getLogger(HelloResource.class);

    @GetMapping("/health")
    public Map<String, String> hello() {
        log.debug("REST request to hello world");
        Map<String, String> map = new HashMap<>();
        map.put("health", "OK");
        map.put("app", "STOKCWAY");
        return map;
    }
}
