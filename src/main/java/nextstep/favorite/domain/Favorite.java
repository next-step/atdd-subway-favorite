package nextstep.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import nextstep.subway.domain.Station;

@Entity
@Table(name = "favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;
    @ManyToOne
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

    public boolean isSameOf(String email) {
        return this.email.equals(email);
    }
}
