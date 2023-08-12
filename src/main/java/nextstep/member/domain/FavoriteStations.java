package nextstep.member.domain;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import nextstep.subway.domain.Station;

@Embeddable
public class FavoriteStations {

    @ManyToOne
    private Station source;

    @ManyToOne
    private Station target;

    public FavoriteStations(Station source, Station target) {
        this.source = source;
        this.target = target;
    }

    public FavoriteStations() {

    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
