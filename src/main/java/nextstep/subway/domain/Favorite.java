package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_id")
    private Station target;

    public Favorite() {
    }

    public Favorite(String email, Station source, Station target) {
        this.email = email;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
