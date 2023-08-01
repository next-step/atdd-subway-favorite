package nextstep.favorite.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import nextstep.subway.applicaion.dto.StationResponse;

@Table(name = "favorite")
@Entity
public class FavoriteResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonIgnore
    private String email;
    @ManyToOne
    @JoinColumn(name = "source_id")
    private StationResponse source;
    @ManyToOne
    @JoinColumn(name = "target_id")
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        Long id = favorite.getId();
        StationResponse source = StationResponse.of(favorite.getSource());
        StationResponse target = StationResponse.of(favorite.getTarget());
        return new FavoriteResponse(id, source, target);
    }

    public long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
