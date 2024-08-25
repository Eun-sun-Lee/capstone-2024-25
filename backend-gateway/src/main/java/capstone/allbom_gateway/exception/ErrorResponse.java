package capstone.allbom_gateway.exception;

public record ErrorResponse(

        String message
) {

    @Override
    public String toString() {
        return "{" +
                "message='" + message + '\'' +
                '}';
    }

}
