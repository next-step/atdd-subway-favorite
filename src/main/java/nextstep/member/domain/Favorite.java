package nextstep.member.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import nextstep.subway.domain.Station;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @Embedded
    private FavoriteStations favoriteStations;

    public Favorite(Member member, FavoriteStations favoriteStations) {
        this.member = member;
        this.favoriteStations = favoriteStations;
    }

    public Favorite() {

    }

    public Long getId() {
        return id;
    }

    public Station getSourceStation() {
        return favoriteStations.getSource();
    }

    public Station getTargetStation() {
        return favoriteStations.getTarget();
    }
}
