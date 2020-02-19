package atdd.week2.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@ToString
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String email;
    private String name;
    private String password;

    @Builder
    public User(String email, String name, String password) {

        this.email = email;
        this.name = name;
        this.password = password;

    }

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .name(this.name)
                .password(this.password)
                .build();
    }

}
