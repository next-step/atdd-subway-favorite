package nextstep.subway.domain;

import nextstep.member.domain.Member;
import nextstep.subway.ui.exception.FavoritesException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    public Favorites() {
    }

    public Favorites(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public Favorites(Long id, Member member, Station source, Station target) {
        this.id = id;
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public boolean canDeleteFavorites(Long loginId) {
        if (id.equals(loginId)) {
            return true;
        }
        throw new FavoritesException("자신의 즐겨찾기만 삭제할 수 있습니다.");
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
