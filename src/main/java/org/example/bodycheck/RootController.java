package org.example.bodycheck;

import org.example.bodycheck.external.redis.service.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RootController {

	private final RedisService redisService;

	@GetMapping("/health")
	public String healthCheck() {
		return "BodyCheck is healthy!";
	}

	@GetMapping("/health/redis")
	public String redisHealthCheck() {
		if ("PONG".equals(redisService.healthCheck())) {
			return "Redis is healthy!";
		} else {
			return "Redis is not healthy!";
		}
	}
}
