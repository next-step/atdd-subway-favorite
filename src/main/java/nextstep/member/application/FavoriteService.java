package nextstep.member.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.application.dto.LoginMemberRequest;
import nextstep.member.application.exception.MemberNotFoundException;
import nextstep.member.application.exception.StationNotFoundException;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final PathService pathService;

    public FavoriteService(
            final MemberRepository memberRepository,
            final StationRepository stationRepository,
            final PathService pathService
    ) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
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

        Favorite favorite = new Favorite(source, target);
        member.addFavorite(favorite);
        memberRepository.flush();
        return favorite.getId();
    }

    private void validateStationConnected(final Station source, final Station target) {
        pathService.findPath(source.getId(), target.getId());
    }

    public List getFavorites(final LoginMemberRequest loginMemberRequest) {
        Member member = memberRepository.findById(loginMemberRequest.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        return member.getFavorites().stream()
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

        member.deleteFavorite(id);
    }
}
