package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.auth.LoginMember;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.exception.FavoriteNotFoundException;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        final var favorite = new Favorite(source.getId(), target.getId(), member.getId());
        favoriteRepository.save(favorite);

        return new FavoriteResponse(favorite, source, target);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(final LoginMember loginMember) {
        final var member = memberService.findById(loginMember.getId());
        final var favorites = favoriteRepository.findByMemberId(member.getId());

        return favorites.stream()
                .map(favorite -> {
                    final var source = stationService.findById(favorite.getSourceId());
                    final var target = stationService.findById(favorite.getTargetId());
                    return new FavoriteResponse(favorite, source, target);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Favorite findById(final Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(FavoriteNotFoundException::new);
    }

    public void removeFavorite(final LoginMember loginMember, final Long id) {
        final var member = memberService.findById(loginMember.getId());
        final var favorite = findById(id);

        favorite.validateRemove(member);

        favoriteRepository.deleteByIdAndMemberId(id, member.getId());
    }
}
