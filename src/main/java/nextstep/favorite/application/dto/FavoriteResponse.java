package nextstep.favorite.application.dto;

import nextstep.station.presentation.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {}

    public FavoriteResponse(Builder builder) {
        this(builder.id, builder.soruce, builder.target);
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
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

    public static class Builder {
        private Long id;
        private StationResponse soruce;
        private StationResponse target;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder source(StationResponse source) {
            this.soruce = source;
            return this;
        }

        public Builder target(StationResponse target) {
            this.target = target;
            return this;
        }

        public FavoriteResponse build() {
            return new FavoriteResponse(this);
        }
    }
}
