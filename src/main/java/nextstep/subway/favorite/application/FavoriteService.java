package nextstep.subway.favorite.application;

import nextstep.subway.dto.PathRequest;
import nextstep.subway.dto.PathResponse;
import nextstep.subway.entity.Station;
import nextstep.subway.exception.NoSuchStationException;
import nextstep.subway.favorite.application.dto.FavoriteRequest;
import nextstep.subway.favorite.application.dto.FavoriteResponse;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.service.PathService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;
    private PathService pathService;
    private MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository
            , StationRepository stationRepository
            , PathService pathService
            , MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.pathService = pathService;
        this.memberService = memberService;
    }

    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());

        Station sourceStation = getStation(request.getSource());
        Station targetStation = getStation(request.getTarget());

        pathService.getPath(new PathRequest(request.getSource(), request.getTarget()));

        Favorite favorite = new Favorite(member, sourceStation, targetStation);

        favoriteRepository.save(favorite);

        return favorite.getId();
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

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NoSuchStationException("존재하지 않는 역입니다."));
    }
}
