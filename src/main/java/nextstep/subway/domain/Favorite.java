package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Station source;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Station target;

    public Favorite() {}
    public Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getId() { return id; }
    public Long getMemberId() { return memberId; }
    public Station getSource() { return source; }
    public Station getTarget() { return target; }
}