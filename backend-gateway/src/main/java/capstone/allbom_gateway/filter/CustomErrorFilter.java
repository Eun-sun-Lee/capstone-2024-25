//package capstone.allbom_gateway.filter;
//import capstone.allbom_gateway.exception.AuthErrorCode;
//import capstone.allbom_gateway.exception.TooManyRequestsException;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
////@Component
////@ControllerAdvice
//public class CustomErrorFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//            // 429 에러일 경우 커스텀 메시지 처리
//            System.out.println("exchange!! = " + exchange.getResponse().getStatusCode());
//            if (exchange.getResponse().getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
//                throw new TooManyRequestsException(AuthErrorCode.TOO_MANY_REQUESTS);
//            }
//        }));
//    }
//
//    @Override
//    public int getOrder() {
//        return 5;  // 필터 순서 조정 (필요시)
//    }
//}