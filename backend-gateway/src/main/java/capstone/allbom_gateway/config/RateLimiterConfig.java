package capstone.allbom_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    private static final String MEMBER_ID_ATTRIBUTE = "MEMBER_ID";

    @Bean
    public KeyResolver memberIdKeyResolver() {
        return exchange -> {
            Long memberId = exchange.getAttribute(MEMBER_ID_ATTRIBUTE);

            if (memberId != null) {
                return Mono.just(memberId.toString()); // MemberId를 기반으로 Rate Limiting 적용
            } else {
                return Mono.just("anonymous"); // MemberId가 없는 경우 기본값 사용
            }
        };
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 1, 1);
    }
}