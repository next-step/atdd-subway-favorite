package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import nextstep.subway.service.StationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberService memberService;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail())
                .orElseThrow(() -> new AuthenticationException("유효한 인증 토큰이 아닙니다."));

        Station sourceStation = stationService.getStationById(request.getSource());
        Station targetStation = stationService.getStationById(request.getTarget());

        return favoriteRepository.save(Favorite.builder()
                .sourceStation(sourceStation)
                .targetStation(targetStation)
                .member(member)
                .build()).getId();
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return null;
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
