package utils;

import java.io.Serializable;
import java.net.SocketAddress;

public class Transit<T extends Serializable> implements Serializable{
    private final TypeCommand type;
    private Serializable[] args;
    private final String userName;
    private final String password;

    private SocketAddress client;

    public Transit(TypeCommand type, Serializable[] args, String userName, String password) {
        this.type = type;
        this.args = args;
        this.userName = userName;
        this.password = password;
    }

    public TypeCommand getType() {
        return type;
    }

    public Serializable[] getArgs() {
        return args;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setClient(SocketAddress client) {
        this.client = client;
    }

    public SocketAddress getClient() {
        return client;
    }
}
