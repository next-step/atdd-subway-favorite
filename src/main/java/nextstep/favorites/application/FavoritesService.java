package nextstep.favorites.application;

import lombok.AllArgsConstructor;
import nextstep.favorites.application.dto.FavoritesResponse;
import nextstep.favorites.domain.Favorites;
import nextstep.favorites.domain.FavoritesRepository;
import nextstep.favorites.application.dto.FavoritesRequest;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FavoritesService {

    private FavoritesRepository favoritesRepository;
    private StationService stationService;

    @Transactional
    public FavoritesResponse save(FavoritesRequest favoritesRequest) {
        List<Station> stations = stationService.findById(favoritesRequest.getSource(), favoritesRequest.getTarget());
        Favorites favorites = favoritesRepository.save(new Favorites(stations.get(0), stations.get(1)));
        return new FavoritesResponse(favorites.getId(), StationResponse.of(favorites.getSource()), StationResponse.of(favorites.getTarget()));
    }

    public List<FavoritesResponse> get() {
        List<FavoritesResponse> list = new ArrayList<>();
        List<Favorites> favoritesList = favoritesRepository.findAll();
        for (Favorites favorites : favoritesList) {
            list.add(FavoritesResponse.of(favorites));
        }
        return list;
    }

    @Transactional
    public void delete(Long id) {
        Favorites favorites = favoritesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("찾는 즐겨찾기가 존재하지 않습니다."));
        favoritesRepository.delete(favorites);
    }
}
