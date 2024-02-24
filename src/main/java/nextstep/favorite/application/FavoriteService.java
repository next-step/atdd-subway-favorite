package nextstep.favorite.application;

import nextstep.exception.ApplicationException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.service.PathService;
import org.springframework.stereotype.Service;

import java.util.List;

import static nextstep.exception.ExceptionMessage.NO_EXISTS_STATION_EXCEPTION;
import static nextstep.favorite.application.dto.FavoriteResponse.listFrom;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.pathService = pathService;
    }

    public FavoriteResponse createFavorite(FavoriteRequest request, LoginMember loginMember) {
        Station source = getStation(request.getSource());
        Station target = getStation(request.getTarget());

        // 경로 확인
        pathService.findShortestPath(source.getId(), target.getId());

        Favorite favorite = new Favorite(source, target, getMember(loginMember));
        favoriteRepository.save(favorite);
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = getMember(loginMember);
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        return listFrom(favorites);
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    private Member getMember(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new AuthenticationException());
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new ApplicationException(NO_EXISTS_STATION_EXCEPTION.getMessage()));
    }
}
