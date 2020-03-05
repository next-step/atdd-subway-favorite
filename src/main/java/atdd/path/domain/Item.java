package atdd.path.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Item {
    private Long id;

    public Item(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
