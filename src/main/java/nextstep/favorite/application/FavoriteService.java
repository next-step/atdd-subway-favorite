package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.line.Line;
import nextstep.subway.line.LineRepository;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;
    private final LineRepository lineRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, LineRepository lineRepository, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * TODO: LoginMember 를 추가로 받아서 FavoriteRequest 내용과 함께 Favorite 를 생성합니다.
     *
     * @param request
     * @return
     */
    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(IllegalArgumentException::new);
        Station sourceStation = stationRepository.findById(request.getSource()).orElseThrow(IllegalArgumentException::new);
        Station targetStation = stationRepository.findById(request.getTarget()).orElseThrow(IllegalArgumentException::new);
        List<Line> lines = lineRepository.findAll();

        Favorite favorite = new Favorite(member.getId(), sourceStation, targetStation, lines);
        favoriteRepository.save(favorite);
        return favorite.getId();
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(IllegalArgumentException::new);
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     *
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
