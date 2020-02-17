package atdd.path.domain.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Getter
public class User extends BaseEntity{
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String password;
}
