package nextstep.member.domain;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.util.List;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;
    @Embedded
    private Favorites favorites = new Favorites();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MEMBER_ROLE", joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    @Column(name = "role")
    private List<String> roles;

    protected Member() {
    }

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = List.of(RoleType.ROLE_MEMBER.name());
    }

    public Member(String email, String password, Integer age, List<String> roles) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = roles;
    }

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }

    public void deleteFavorite(Long id) {
        favorites.delete(id);
    }

    public void update(String email, Integer age) {
        this.email = email;
        this.age = age;
    }

    public List<Favorite> getFavorites() {
        return favorites.getFavorites();
    }

    public Long getId() {
        return id;
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

    public List<String> getRoles() {
        return roles;
    }

    public Favorite getFavoriteByStationIds(Long source, Long target) {
        return favorites.matchingFavorite(source, target);
    }
}
