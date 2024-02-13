package nextstep.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.exception.UnauthorizedDeletionException;
import nextstep.favorite.CannotFavoriteNonexistentPathException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.path.PathFinder;
import nextstep.station.Station;
import nextstep.station.StationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    private final StationRepository stationRepository;

    private final PathFinder pathFinder;

    public Long createFavorite(FavoriteRequest request, Member member) {
        if (!pathFinder.pathExists(request.getSource() + "", request.getTarget() + "")) {
            throw new CannotFavoriteNonexistentPathException();
        }

        Station sourceStation = stationRepository.findById(request.getSource()).orElseThrow(EntityNotFoundException::new);
        Station targetStation = stationRepository.findById(request.getTarget()).orElseThrow(EntityNotFoundException::new);

        Favorite favorite = Favorite.of(sourceStation, targetStation, member);
        return favoriteRepository.save(favorite).getId();
    }

    public List<FavoriteResponse> findFavorites(Member member) {
        return favoriteRepository.findAllByMember(member).stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id, Member member) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!favorite.getMember().equals(member)) {
            throw new UnauthorizedDeletionException();
        }
        favoriteRepository.deleteById(id);
    }
}
