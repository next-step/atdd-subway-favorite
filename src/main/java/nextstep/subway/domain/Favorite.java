package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    private Long member;

    private Long source;

    private Long target;

    protected Favorite() {
    }

    public Favorite(final Long member, final Long source, final Long target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Long getMember() {
        return member;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
