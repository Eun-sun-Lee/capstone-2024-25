package capstone.allbom_gateway.jwt;

public record TokenPayload(
        Long memberId,
        Long iat,
        Long exp
) {
}

