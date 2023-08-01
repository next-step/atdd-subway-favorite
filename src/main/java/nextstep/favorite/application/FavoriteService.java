package nextstep.favorite.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.BadRequestException;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public long create(String email, long source, long target) {
        Station sourceStation = stationRepository.findById(source).orElseThrow(BadRequestException::new);
        Station tartgetStation = stationRepository.findById(target).orElseThrow(BadRequestException::new);
        Favorite favorite = new Favorite(email, sourceStation, tartgetStation);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return savedFavorite.getId();
    }

    @Transactional
    public void delete(String email, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(BadRequestException::new);
        if (!favorite.isSameOf(email)) {
            throw new AuthenticationException();
        }
        favoriteRepository.deleteById(id);
    }
}
