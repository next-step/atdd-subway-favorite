package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.auth.LoginMember;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteResponse addFavorite(final LoginMember loginMember, final FavoriteRequest favoriteRequest) {
        final var source = stationService.findById(favoriteRequest.getSource());
        final var target = stationService.findById(favoriteRequest.getTarget());

        final var member = memberService.findById(loginMember.getId());

        final var favorite = new Favorite(source, target, member);
        favoriteRepository.save(favorite);

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(final LoginMember loginMember) {
        final var member = memberService.findById(loginMember.getId());
        final var favorites = favoriteRepository.findByMember(member);

        return FavoriteResponse.of(favorites);
    }
}
