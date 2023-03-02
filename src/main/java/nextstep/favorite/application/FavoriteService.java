package nextstep.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.application.dto.LoginMemberRequest;
import nextstep.member.application.exception.MemberNotFoundException;
import nextstep.member.application.exception.StationNotFoundException;
import nextstep.member.application.exception.UnAuthorizedException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;
    private final PathService pathService;

    public FavoriteService(
            final MemberRepository memberRepository,
            final StationRepository stationRepository,
            final FavoriteRepository favoriteRepository,
            final PathService pathService
    ) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
        this.pathService = pathService;
    }

    @Transactional
    public Long register(final LoginMemberRequest loginMemberRequest, final FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(loginMemberRequest.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        Station source = stationRepository.findById(favoriteRequest.getSource())
                .orElseThrow(StationNotFoundException::new);
        Station target = stationRepository.findById(favoriteRequest.getTarget())
                .orElseThrow(StationNotFoundException::new);

        validateStationConnected(source, target);

        Favorite favorite = new Favorite(member.getId(), source, target);
        favoriteRepository.save(favorite);
        return favorite.getId();
    }

    private void validateStationConnected(final Station source, final Station target) {
        pathService.findPath(source.getId(), target.getId());
    }

    public List getFavorites(final LoginMemberRequest loginMemberRequest) {
        Member member = memberRepository.findById(loginMemberRequest.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        return favoriteRepository.findByMemberId(member.getId()).stream()
                .map(this::createFavoriteResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public FavoriteResponse createFavoriteResponse(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget())
        );
    }

    @Transactional
    public void delete(final LoginMemberRequest loginMemberRequest, final Long id) {
        Member member = memberRepository.findById(loginMemberRequest.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Favorite favorite = favoriteRepository.findByMemberId(member.getId()).stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElseThrow(UnAuthorizedException::new);

        favoriteRepository.delete(favorite);
    }
}
