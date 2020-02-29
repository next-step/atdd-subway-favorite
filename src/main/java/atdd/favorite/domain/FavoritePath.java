package atdd.favorite.domain;

import atdd.favorite.application.dto.FavoritePathRequestView;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FavoritePath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Long startId;
    private Long endId;

    public FavoritePath() {
    }

    public FavoritePath(String email, Long startId, Long endId) {
        this(0L, email, startId, endId);
    }

    public FavoritePath(Long id, String email, Long startId, Long endId) {
        this.id = id;
        this.email = email;
        this.startId = startId;
        this.endId = endId;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Long getStartId() {
        return startId;
    }

    public Long getEndId() {
        return endId;
    }

    public static FavoritePath of(FavoritePathRequestView requestView){
        return new FavoritePath(
                requestView.getEmail(),
                requestView.getStartId(),
                requestView.getEndId());
    }
}
