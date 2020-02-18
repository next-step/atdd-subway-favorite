package atdd.path.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table
public class User extends BaseEntity {
    @Column
    private String name;
    @Column
    private String email;

    @Setter
    @Column
    private String password;

    @Builder
    private User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
