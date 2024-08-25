package capstone.allbom_gateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret}")
    private String secretKey;
    private final TokenProcessor tokenProcessor;

    private static final String MEMBER_ID_ATTRIBUTE = "MEMBER_ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (token == null || !token.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }


        try {
//            Claims claims = validateToken(token);
//            Long memberId = Long.parseLong(claims.getSubject());
//            System.out.println("memberId = " + memberId);
            final String tokenWithoutType = tokenProcessor.extractAccessToken(token);
            System.out.println("tokenWithoutType = " + tokenWithoutType);

            tokenProcessor.validateToken(tokenWithoutType);
            final TokenPayload tokenPayload = tokenProcessor.decodeToken(tokenWithoutType);
            System.out.println("tokenPayload.memberId = " + tokenPayload.memberId());
            exchange.getAttributes().put(MEMBER_ID_ATTRIBUTE, tokenPayload.memberId()); // MemberId 저장
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private Claims validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public int getOrder() {
        return -1; // 필터의 우선순위 지정 (음수는 높은 우선순위)
    }
}

