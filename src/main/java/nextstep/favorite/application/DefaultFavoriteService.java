package nextstep.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.domain.FavoriteService;
import nextstep.member.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.domain.model.Station;
import nextstep.subway.domain.service.PathService;
import nextstep.subway.domain.service.StationService;

@Service
public class DefaultFavoriteService implements FavoriteService {
    public static final String NOT_FOUND_FAVORITE_MESSAGE = "존재하지 않는 즐겨찾기입니다.";

    public static final String PATH_NOT_FOUND_MESSAGE = "존재하지 않는 경로입니다.";

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;
    private final PathService pathService;

    public DefaultFavoriteService(FavoriteRepository favoriteRepository, MemberService memberService,
        StationService stationService, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    public Favorite createFavorite(FavoriteRequest request, LoginMember loginMember) {
        Member member = memberService.findMemberByEmailOrThrow(loginMember.getEmail());
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());

        if (!pathService.pathExists(source.getId(), target.getId())) {
            throw new IllegalArgumentException(PATH_NOT_FOUND_MESSAGE);
        }

        Favorite favorite = new Favorite(source.getId(), target.getId(), member.getId());
        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberService.findMemberByEmailOrThrow(loginMember.getEmail());

        return favoriteRepository.findByMemberId(member.getId())
            .stream()
            .map(this::buildFavoriteResponse)
            .collect(Collectors.toList());
    }

    private FavoriteResponse buildFavoriteResponse(Favorite favorite) {
        return FavoriteResponse.of(
            favorite.getId(),
            stationService.findStationById(favorite.getSourceStationId()),
            stationService.findStationById(favorite.getTargetStationId())
        );
    }

    public void deleteFavorite(Long id, LoginMember loginMember) {
        Member foundMember = memberService.findMemberByEmailOrThrow(loginMember.getEmail());

        if (foundMember == null) {
            throw new AuthenticationException();
        }

        favoriteRepository.deleteById(id);
    }
}
