package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToOne
    @JoinColumn(name = "source_station_id")
    private Station source;

    @OneToOne
    @JoinColumn(name = "target_station_id")
    private Station target;

    public Favorite() {
    }

    public Favorite(Long id, Long userId, Station source, Station target) {
        this.id = id;
        this.userId = userId;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Long userId, Station source, Station target) {
        return new Favorite(null, userId, source, target);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
