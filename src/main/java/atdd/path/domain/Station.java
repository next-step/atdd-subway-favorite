package atdd.path.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Transient
    private List<Line> lines = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sourceStation")
    private List<Edge> sourceEdges = new ArrayList();

    @JsonIgnore
    @OneToMany(mappedBy = "targetStation")
    private List<Edge> targetEdge = new ArrayList();

    @OneToOne(cascade = CascadeType.ALL, mappedBy="station")
    @PrimaryKeyJoinColumn
    private FavoriteStation favoriteStation;

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


    public List<Line> getLines() {
        return Stream.concat(sourceEdges.stream(), targetEdge.stream())
                .map(it -> it.getLine())
                .collect(Collectors.toList());
    }
}
