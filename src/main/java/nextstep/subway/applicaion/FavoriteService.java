package nextstep.subway.applicaion;

import nextstep.exception.NotExistAuthorizationException;
import nextstep.exception.NotFoundMemberException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public FavoriteService(
            final FavoriteRepository favoriteRepository,
            final StationService stationService,
            final MemberRepository memberRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse saveFavorite(final Long memberId, final FavoriteRequest request) {
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());
        Member member = findMemberById(memberId);

        Favorite favorite = Favorite.of(source, target, member);
        Favorite createdFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(createdFavorite);
    }

    public List<FavoriteResponse> findAllFavorites(final Long memberId) {
        Member member = findMemberById(memberId);
        return findAllMyFavorites(member).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavoriteById(final Long favoriteId, final Long memberId) {
        if(isNotMyFavorite(favoriteId, memberId)) {
            throw new NotExistAuthorizationException();
        }
        favoriteRepository.deleteById(favoriteId);
    }

    private boolean isNotMyFavorite(final Long favoriteId, final Long memberId) {
        Member member = findMemberById(memberId);
        return findAllMyFavorites(member).stream()
                .noneMatch(f -> f.getId().equals(favoriteId));
    }

    private Member findMemberById(final Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
    }

    private List<Favorite> findAllMyFavorites(final Member member) {
        return favoriteRepository.findAllByMember(member);
    }
}
