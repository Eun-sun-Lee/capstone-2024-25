package capstone.allbom_gateway.exception;

public record ExceptionResponse(
        int code,

        String message
) {

    public static ExceptionResponse from(final AllbomException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return new ExceptionResponse(errorCode.getCode(), errorCode.getMessage());
    }

}
