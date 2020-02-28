package atdd.path.domain;

import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public class Item {
    private Long id;
    private String name;

    public Item(String name) {
        this.name = name;
    }

    public Item(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item station = (Item) o;
        return Objects.equals(id, station.id) &&
                Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
