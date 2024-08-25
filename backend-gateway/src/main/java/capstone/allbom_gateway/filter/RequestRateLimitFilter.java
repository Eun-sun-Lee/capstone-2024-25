package capstone.allbom_gateway.filter;


import capstone.allbom_gateway.exception.AuthErrorCode;
import capstone.allbom_gateway.exception.TooManyRequestsException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.support.HasRouteId;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.springframework.web.server.ServerWebExchange;

@Component
@Slf4j
public class RequestRateLimitFilter extends AbstractGatewayFilterFactory<RequestRateLimitFilter.Config> {

    private final KeyResolver memberIdKeyResolver;
    private final RedisRateLimiter defaultRateLimiter;

    public RequestRateLimitFilter(KeyResolver memberIdKeyResolver, RedisRateLimiter redisRateLimiter) {
        super(Config.class);
        this.memberIdKeyResolver = memberIdKeyResolver;
        this.defaultRateLimiter = redisRateLimiter;
    }

    @Override
    public GatewayFilter apply(Config config) {
        GatewayFilter filter = (exchange, chain) -> {
            KeyResolver keyResolver = getOrDefault(config.keyResolver, memberIdKeyResolver);
            RedisRateLimiter rateLimiter = getOrDefault(config.rateLimiter, defaultRateLimiter);
            String routeId = config.getRouteId();

            System.out.println("routeId = " + routeId);

            return keyResolver.resolve(exchange)
                    .flatMap(key -> rateLimiter.isAllowed(routeId, key))
                    .flatMap(rateLimitResponse -> {
                        if (rateLimitResponse.isAllowed()) {
                            return chain.filter(exchange);  // Rate limit이 허용된 경우
                        } else {
                            log.warn("Rate limit exceeded for key: ");
                            // TooManyRequestException 발생
                            return Mono.error(new TooManyRequestsException(AuthErrorCode.TOO_MANY_REQUESTS));
                        }
                    });
        };

        return filter;  // 필터를 리턴
    }


//    private Mono<Void> handleRateLimitExceeded(ServerWebExchange exchange) {
//        // TooManyRequestsException을 발생시킴
//        return Mono.error(new TooManyRequestsException(AuthErrorCode.TOO_MANY_REQUESTS));
//    }

    private <T> T getOrDefault(T configValue, T defaultValue) {
        return configValue != null ? configValue : defaultValue;
    }

    @Getter
    @Setter
    public static class Config implements HasRouteId {
        private KeyResolver keyResolver;
        private RedisRateLimiter rateLimiter;
        private String routeId;
    }
}

