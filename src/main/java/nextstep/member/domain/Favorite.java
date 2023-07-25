package nextstep.member.domain;

import nextstep.exception.favoriteException.FavoriteException;
import nextstep.exception.pathexception.NotConnectedPathException;
import nextstep.subway.applicaion.PathFinder;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() {
    }

    protected Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Long memberId, Station source, Station target, List<Section> sections) {
        try {
            PathFinder.find(source.getId(), target.getId(), sections);
            return new Favorite(memberId, source, target);
        } catch (NotConnectedPathException e) {
            throw new FavoriteException(e.getMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
