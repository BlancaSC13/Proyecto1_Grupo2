package domain.System;

public class Security {
    private String user;
    private  String password;


    public Security(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public Security(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "Security{" +
                "User='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
