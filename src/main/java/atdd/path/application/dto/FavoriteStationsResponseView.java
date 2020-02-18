package atdd.path.application.dto;

import atdd.path.domain.FavoriteStation;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FavoriteStationsResponseView {

    private int count;
    private List<FavoriteStationResponseView> favorites;

    private FavoriteStationsResponseView() {}

    public FavoriteStationsResponseView(List<FavoriteStation> favorites) {
        this.count = favorites.size();
        this.favorites = favorites.stream()
                .map(FavoriteStationResponseView::new)
                .collect(toList());
    }

    public int getCount() {
        return count;
    }

    public List<FavoriteStationResponseView> getFavorites() {
        return favorites;
    }

}
