package nextstep.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Favorite() {
    }

    public Favorite(Station target, Station source, Member member) {

    }

    public boolean isCreateUser(Member member) {
        return true;
    }
}
