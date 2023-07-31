package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final PathService pathService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, PathService pathService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.pathService = pathService;
        this.memberService = memberService;
    }

    @Transactional
    public FavoriteResponse saveFavorite(FavoriteSaveRequest favoriteSaveRequest, String email) {

        Member member = memberService.findByEmail(email);

        Station sourceStation = stationService.findById(favoriteSaveRequest.getSource());
        Station targetStation = stationService.findById(favoriteSaveRequest.getTarget());

        pathService.validatePath(sourceStation, targetStation);

        Favorite favorite = new Favorite(sourceStation, targetStation, member);
        Favorite savedFavorite = favoriteRepository.save(favorite);

        return FavoriteResponse.of(savedFavorite);
    }

    public FavoriteResponse findFavoritesById(Long favoriteId, String email) {

        Member member = memberService.findByEmail(email);

        return FavoriteResponse.of(member.getFavorite(favoriteId));
    }

    public List<FavoriteResponse> findFavoritesByEmail(String email) {

        Member member = memberService.findByEmail(email);

        return member.getFavorites().stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long favoriteId, String email) {

        Member member = memberService.findByEmail(email);

        Favorite favorite = member.getFavorite(favoriteId);

        member.removeFavorite(favorite);
    }
}
