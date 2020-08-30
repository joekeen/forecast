package au.id.keen.forecast.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String email;

    @Column(length = 60, name = "password_hash")
    private String passwordHash;

    @Version
    private Integer version;

}
