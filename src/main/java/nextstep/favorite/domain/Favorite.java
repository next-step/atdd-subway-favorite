package nextstep.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private long source;
    private long target;

    public Favorite() {
    }

    public Favorite(String email, long source, long target) {
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

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }
}
