package atdd.favorite.service;

import atdd.favorite.application.dto.FavoritePathListResponseView;
import atdd.favorite.application.dto.FavoritePathRequestView;
import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.favorite.domain.FavoritePath;
import atdd.favorite.domain.FavoritePathRepository;
import atdd.path.application.GraphService;
import atdd.path.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FavoritePathService {
    private FavoritePathRepository favoritePathRepository;
    private GraphService graphService;

    public FavoritePathService(FavoritePathRepository favoritePathRepository,
                               GraphService graphService) {
        this.favoritePathRepository = favoritePathRepository;
        this.graphService = graphService;
    }

    public FavoritePathResponseView create(FavoritePathRequestView requestView)
            throws Exception {
        if (requestView.getStartId() == requestView.getEndId()) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
        FavoritePath savedFavoritePath
                = favoritePathRepository.save(FavoritePath.of(requestView));
        List<Station> favoritePathStations
                = graphService.findPath(requestView.getStartId(), requestView.getEndId());
        return FavoritePathResponseView.of(savedFavoritePath, favoritePathStations);
    }

    public void delete(FavoritePathRequestView requestView) throws Exception {
        Optional<FavoritePath> favoritePathById
                = favoritePathRepository.findById(requestView.getId());
        if (Optional.empty().isPresent()) {
            throw new NoSuchElementException("즐겨찾기에 등록되지 않은 지하철경로입니다.");
        }
        if (favoritePathById.get().getEmail() != requestView.getEmail()) {
            throw new NoSuchElementException("즐겨찾기를 등록한 사람만 삭제할 수 있습니다.");
        }
        favoritePathRepository.delete(FavoritePath.of(requestView));
    }

    public FavoritePathListResponseView showAllFavoritePath(FavoritePathRequestView requestView) {
        Optional<List<FavoritePath>> allByEmail
                = favoritePathRepository.findAllByEmail(requestView.getEmail());
        if(Optional.empty().isPresent()){
            throw new NoSuchElementException("등록된 지하철경로가 없습니다.");
        }
        return new FavoritePathListResponseView(requestView.getEmail(), allByEmail.get());
    }
}
