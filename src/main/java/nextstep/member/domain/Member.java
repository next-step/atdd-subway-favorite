package nextstep.member.domain;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.Fovorites;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE member SET deleted_at = CURRENT_TIMESTAMP where member_id = ?")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(unique = true)
    private String email;

    private String password;

    private Integer age;

    @Embedded
    private Fovorites fovorites = new Fovorites();

    @Column
    private LocalDateTime deletedAt;

    public Member() {
    }

    private Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static Member of(String email, String password, Integer age) {
        return new Member(email, password, age);
    }

    public void addFavorite(Favorite favorite) {
        this.fovorites.add(favorite);
        favorite.setMember(this);
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<Favorite> getFavorites() {
        return this.fovorites.getFavorites();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

}
