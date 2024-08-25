package capstone.allbom_gateway.exception;

public class BadRequestException extends AllbomException {

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}

