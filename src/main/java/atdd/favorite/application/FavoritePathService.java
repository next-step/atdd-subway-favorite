package atdd.favorite.application;

import atdd.favorite.application.dto.CreateFavoritePathRequestView;
import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.favorite.domain.FavoritePath;
import atdd.favorite.domain.FavoritePathRepository;
import atdd.path.application.GraphService;
import atdd.path.domain.Station;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class FavoritePathService {
    private FavoritePathRepository repository;
    private GraphService graphService;

    private FavoritePathService(FavoritePathRepository repository, GraphService graphService) {
        this.repository = repository;
        this.graphService = graphService;
    }

    public FavoritePathResponseView create(CreateFavoritePathRequestView request) {
        FavoritePath favoritePath = repository.save(request.toEntity());
        List<Station> favoritePathStations
                = graphService.findPath(request.getStartStationId(), request.getEndStationId());
        return new FavoritePathResponseView(
                favoritePath.getId(),
                favoritePath.getUserEmail(),
                favoritePathStations);
    }

    public void delete(Long id) {
        FavoritePath favoritePath = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 경로가 존재하지 않습니다."));
        repository.delete(favoritePath);
    }
}
