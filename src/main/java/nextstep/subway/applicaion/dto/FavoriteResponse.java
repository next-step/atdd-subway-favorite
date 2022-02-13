package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;

public class FavoriteResponse {

    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public FavoriteResponse(final Favorite favorite) {
        this(favorite.getId(), favorite.getSource(), favorite.getTarget());
    }

    public FavoriteResponse(final Long id, final Station source, final Station target) {
        this.id = id;
        this.source = new StationResponse(source);
        this.target = new StationResponse(target);
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    private static class StationResponse {
        private final Long id;
        private final String name;
        private final LocalDateTime createdDate;
        private final LocalDateTime modifiedDate;

        public StationResponse(final Station station) {
            this(station.getId(), station.getName(), station.getCreatedDate(), station.getModifiedDate());
        }

        public StationResponse(final Long id, final String name, final LocalDateTime createdDate, final LocalDateTime modifiedDate) {
            this.id = id;
            this.name = name;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public LocalDateTime getCreatedDate() {
            return createdDate;
        }

        public LocalDateTime getModifiedDate() {
            return modifiedDate;
        }
    }
}
