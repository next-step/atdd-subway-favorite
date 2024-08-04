package nextstep.line.application.dto;

import nextstep.station.application.dto.StationResponse;

import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String color;
        private List<StationResponse> stations;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder stations(List<StationResponse> stations) {
            this.stations = stations;
            return this;
        }

        public LineResponse build() {
            LineResponse lineResponse = new LineResponse();
            lineResponse.id = this.id;
            lineResponse.name = this.name;
            lineResponse.color = this.color;
            lineResponse.stations = this.stations;
            return lineResponse;
        }
    }
}
