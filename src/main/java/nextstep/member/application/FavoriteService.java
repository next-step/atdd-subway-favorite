package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(Member member, FavoriteRequest farvoriteRequest) {
        Station sourceStation = stationRepository.findById(farvoriteRequest.getSource()).orElseThrow(IllegalArgumentException::new);
        Station targetStation = stationRepository.findById(farvoriteRequest.getTarget()).orElseThrow(IllegalArgumentException::new);

        Favorite favorite = favoriteRepository.save(Favorite.builder()
                .member(member)
                .sourceStation(sourceStation)
                .targetStation(targetStation).build());

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> showFavorite(Member member) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        return FavoriteResponse.listOf(favorites);
    }

    public void deleteFavorite(Member member, Long id) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());

        Favorite favorite = favorites.stream().filter(a -> id.equals(a.getId())).findFirst().orElseThrow(IllegalArgumentException::new);
        favoriteRepository.delete(favorite);
    }
}
