package atdd.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Station extends Item {
    private Long id;
    private String name;
    private List<Line> lines = new ArrayList<>();

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(Long id, String name, List<Line> lines) {
        this.id = id;
        this.name = name;
        this.lines = lines;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public List<Line> getLines() {
        return lines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) &&
                Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
