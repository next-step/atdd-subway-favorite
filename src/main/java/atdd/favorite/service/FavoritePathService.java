package atdd.favorite.service;

import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.favorite.application.dto.FavoritePathRequestView;
import atdd.favorite.domain.FavoritePath;
import atdd.favorite.domain.FavoritePathRepository;
import atdd.path.application.GraphService;
import atdd.path.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritePathService {
    private FavoritePathRepository favoritePathRepository;
    private GraphService graphService;

    public FavoritePathService(FavoritePathRepository favoritePathRepository,
                               GraphService graphService) {
        this.favoritePathRepository = favoritePathRepository;
        this.graphService = graphService;
    }

    public FavoritePathResponseView create(FavoritePathRequestView requestView) {
        FavoritePath savedFavoritePath
                = favoritePathRepository.save(FavoritePath.of(requestView));
        List<Station> favoritePathStations
                = graphService.findPath(requestView.getStartId(), requestView.getEndId());
        return FavoritePathResponseView.of(savedFavoritePath, favoritePathStations);
    }
}
