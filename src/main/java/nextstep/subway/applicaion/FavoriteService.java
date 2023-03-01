package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteReadResponse;
import nextstep.subway.applicaion.message.Message;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public Favorite createFavorite(FavoriteCreateRequest request) {
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        if (source.equals(target)) {
            throw new IllegalArgumentException(Message.SOURCE_TARGET_DUPLICATE_STATION.getMessage());
        }
        Favorite favorite = favoriteRepository.save(new Favorite(source, target));
        return favorite;
    }

    @Transactional(readOnly = true)
    public List<FavoriteReadResponse> readFavorites() {
        return favoriteRepository.findAll()
                .stream()
                .map(FavoriteReadResponse::new)
                .collect(toList());
    }

    @Transactional
    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

}
