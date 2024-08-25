package capstone.allbom_gateway.exception;

public class ExpiredPeriodJwtException extends AllbomException{
    public ExpiredPeriodJwtException(ErrorCode errorCode) {
        super(errorCode);
    }
}