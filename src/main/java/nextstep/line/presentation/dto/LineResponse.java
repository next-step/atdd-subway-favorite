package nextstep.line.presentation.dto;

public class LineResponse {

    private final Long id;
    private String name;
    private String color;
    private int distance;
    private SectionsResponse sections;

    private LineResponse(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.color = builder.color;
        this.distance = builder.distance;
        this.sections = builder.sections;
    }

    public static class Builder {
        private final Long id;
        private String name;
        private String color;
        private int distance;
        private SectionsResponse sections;

        public Builder(Long id) {
            this.id = id;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder sections(SectionsResponse sections) {
            this.sections = sections;
            return this;
        }

        public LineResponse build() {
            return new LineResponse(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getDistance() {
        return distance;
    }

    public SectionsResponse getSections() {
        return sections;
    }
}
