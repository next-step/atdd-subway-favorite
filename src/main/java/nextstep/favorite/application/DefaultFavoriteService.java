package nextstep.favorite.application;

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

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import java.util.stream.Collectors;

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
        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());

        if (!pathService.pathExists(source.getId(), target.getId())) {
            throw new IllegalArgumentException(PATH_NOT_FOUND_MESSAGE);
        }

        Favorite favorite = new Favorite(source, target, member);
        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());

        return findFavoritesByMember(member).stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    private List<Favorite> findFavoritesByMember(Member member) {
        List<Favorite> favorites = favoriteRepository.findByMember(member);

        if (CollectionUtils.isEmpty(favorites)) {
            return Collections.emptyList();
        }

        return favorites;
    }

    public void deleteFavorite(Long id, LoginMember loginMember) {
        Favorite favorite = findFavoriteByIdOrThrow(id);

        if (!favorite.matchesMemberEmail(loginMember.getEmail())) {
            throw new AuthenticationException();
        }

        favoriteRepository.deleteById(id);
    }

    private Favorite findFavoriteByIdOrThrow(Long id) {
        return favoriteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_FAVORITE_MESSAGE));
    }
}
