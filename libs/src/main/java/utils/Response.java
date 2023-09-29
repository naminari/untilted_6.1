package utils;

import java.io.Serializable;

public class Response implements Serializable {
    private final TypeResponse response;
    private final String message;

    public Response(TypeResponse response, String message) {
        this.response = response;
        this.message = message;
    }

    public TypeResponse getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }
}