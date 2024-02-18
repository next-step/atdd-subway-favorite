package nextstep.subway.favorite.application;


import nextstep.subway.favorite.application.dto.FavoriteRequest;
import nextstep.subway.favorite.application.dto.FavoriteResponse;
import nextstep.subway.favorite.application.dto.FavoriteResponseFactory;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.line.LineRepository;
import nextstep.subway.line.Lines;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.PathFinder;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private final PathFinder pathFinder;
    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(PathFinder pathFinder,
                           LineRepository lineRepository,
                           MemberRepository memberRepository,
                           StationRepository stationRepository,
                           FavoriteRepository favoriteRepository) {
        this.pathFinder = pathFinder;
        this.lineRepository = lineRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    /**
     *
     * @param loginMember
     * @param request
     */
    public void createFavorite(LoginMember loginMember,
                               FavoriteRequest request) {
        Favorite favorite = new Favorite(pathFinder,
                Lines.from(lineRepository.findAllFetchJoin()),
                getStation(request.getSource()),
                getStation(request.getTarget()),
                getMember(loginMember));
        favoriteRepository.save(favorite);
    }

    /**
     *
     * @param loginMember
     * @return
     */
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = getMember(loginMember);
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        return FavoriteResponseFactory.createFavoriteResponse(favorites);
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     *
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("해당 지하철역 정보를 찾지 못했습니다."));
    }

    private Member getMember(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾지 못했습니다."));
    }
}
