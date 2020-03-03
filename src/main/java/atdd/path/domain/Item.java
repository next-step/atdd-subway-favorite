package atdd.path.domain;

import lombok.NoArgsConstructor;

import static atdd.path.dao.FavoriteDao.EDGE_TYPE;

@NoArgsConstructor
public class Item {
    private Long id;

    public Item(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void validateFavoriteEdge(String type) {
        if (EDGE_TYPE.equals(type)) {
            Edge edge = (Edge) this;
            edge.checkConnectSourceAndTarget();
            edge.checkSourceAndTargetStationIsSameWhenEdge();
        }
    }
}
