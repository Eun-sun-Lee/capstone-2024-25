package capstone.allbom_gateway.exception;

public class TooManyRequestsException extends AllbomException{
    public TooManyRequestsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
