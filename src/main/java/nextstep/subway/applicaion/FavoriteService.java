package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.repository.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.NotFoundStationException;
import nextstep.subway.exception.NotMyFavoriteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(String email, FavoriteRequest request) {
        Station source = stationRepository.findById(request.getSource())
                .orElseThrow(() -> new NotFoundStationException("잘못된 source id입니다."));
        Station target = stationRepository.findById(request.getTarget())
                .orElseThrow(() -> new NotFoundStationException("잘못된 target id입니다."));

        Favorite save = favoriteRepository.save(new Favorite(email, source, target));
        return FavoriteResponse.of(save);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getAllFavorite(String email) {
        return favoriteRepository.findAllByEmail(email).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FavoriteResponse getFavorite(Long id, String email) {
        return favoriteRepository.findByIdAndEmail(id, email)
                .map(FavoriteResponse::of)
                .orElseThrow(() -> new NotMyFavoriteException("내가 저장한 즐겨찾기가 아닙니다."));
    }

    @Transactional
    public void deleteFavorite(Long id, String email) {
        favoriteRepository.deleteByIdAndEmail(id, email);
    }
}
