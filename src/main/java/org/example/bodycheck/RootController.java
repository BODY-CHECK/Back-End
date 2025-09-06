package org.example.bodycheck;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.redis.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RootController {

    private final RedisService redisService;

    @GetMapping("/health")
    public String healthCheck() { return "BodyCheck is healthy!"; }

    @GetMapping("/health/redis")
    public String redisHealthCheck() {
        if ("PONG".equals(redisService.healthCheck())) {
            return "Redis is healthy!";
        }
        else {
            return "Redis is not healthy!";
        }
    }
}
