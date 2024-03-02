package nextstep.favorite.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source")
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target")
    private Station target;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "create_user")
    private Member createUser;

    public Favorite() {
    }

    public Favorite(Station source, Station target, Member createUser) {
        this.source = source;
        this.target = target;
        this.createUser = createUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Station getSource() {
        return source;
    }

    public void setSource(Station source) {
        this.source = source;
    }

    public Station getTarget() {
        return target;
    }

    public void setTarget(Station target) {
        this.target = target;
    }

    public Member getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Member createUser) {
        this.createUser = createUser;
    }

    public boolean isCreateUser(Member loginMember) {
        return loginMember.equals(createUser);
    }
}
