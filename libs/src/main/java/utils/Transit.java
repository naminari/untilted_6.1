package utils;

import java.io.Serializable;

public class Transit<T extends Serializable> implements Serializable{
    private final TypeCommand type;
    private Serializable[] args;

    public Transit(TypeCommand type, Serializable[] args) {
        this.type = type;
        this.args = args;
    }

    public TypeCommand getType() {
        return type;
    }

    public Serializable[] getArgs() {
        return args;
    }
}
