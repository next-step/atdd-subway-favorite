package atdd.path.domain;

import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public class Item {
    private Long id;

    public Item(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item station = (Item) o;
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
