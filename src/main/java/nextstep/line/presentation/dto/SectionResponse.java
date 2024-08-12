package nextstep.line.presentation.dto;

public class SectionResponse {
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    private SectionResponse(Builder builder) {
        this.id = builder.id;
        this.upStationId = builder.upStationId;
        this.downStationId = builder.downStationId;
        this.distance = builder.distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public static class Builder {
        private Long id;
        private Long upStationId;
        private Long downStationId;
        private int distance;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public SectionResponse build() {
            return new SectionResponse(this);
        }
    }
}
