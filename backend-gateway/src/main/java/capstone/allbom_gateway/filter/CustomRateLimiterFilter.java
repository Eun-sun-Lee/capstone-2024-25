//package capstone.allbom_gateway.filter;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
//import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
//import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class CustomRateLimiterFilter extends RequestRateLimiterGatewayFilterFactory {
//
//    @Autowired
//    public CustomRateLimiterFilter(RedisRateLimiter redisRateLimiter, KeyResolver keyResolver) {
//        super(redisRateLimiter, keyResolver);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        GatewayFilter originalFilter = super.apply(config);
//
//        return (exchange, chain) -> originalFilter.filter(exchange, chain)
//                .then(Mono.defer(() -> {
//                    // 응답이 완료된 후 상태 코드를 확인하고 로깅 또는 응답 처리
//                    if (exchange.getResponse().getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
//                        System.out.println("Custom handling for 429 - Too Many Requests");
//
//                        // 응답 헤더 추가
//                        exchange.getResponse().getHeaders().set("X-RateLimit-Error", "Custom Error Message");
//
//                        // 응답 본문 또는 추가 작업 처리
//                    }
//                    return Mono.empty();
//                }));
//    }
//}
