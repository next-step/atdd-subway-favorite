package atdd.favorite.service;

import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.favorite.application.dto.FavoritePathRequestView;
import atdd.favorite.domain.FavoritePathRepository;
import atdd.path.application.GraphService;
import org.springframework.stereotype.Service;

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
        return null;
    }
}
