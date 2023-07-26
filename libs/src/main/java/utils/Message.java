package utils;

import java.io.Serializable;

public class Message implements Serializable {
    private final String message;
    private final TypeMessage typeMessage;

    public Message(TypeMessage typeMessage, String message) {
        this.message = message;
        this.typeMessage = typeMessage;
    }

    public String getMessage() {
        return message;
    }

    public TypeMessage getTypeMessage() {
        return typeMessage;
    }
}
