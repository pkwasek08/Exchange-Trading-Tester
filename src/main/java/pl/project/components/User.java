package pl.project.components;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class User {
    private int id;
    private String name;
    private String lastname;
    private String email;
    private Timestamp created_at;
    private Timestamp birthday;
    private String login;
    private String password;
    private Float cash;

    public User(String name, String lastname, String email, String password, Float cash) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.cash = cash;
        this.created_at = new Timestamp(System.currentTimeMillis());
    }
}
