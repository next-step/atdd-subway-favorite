package nextstep.favorite.application;

import nextstep.exception.BadRequestException;
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
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository, LineRepository lineRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(
                () -> new BadRequestException("사용자 정보를 찾을 수 없습니다.")
        );
        Station sourceStation = stationRepository.findById(request.getSource()).orElseThrow(
                () -> new BadRequestException("출발지 역을 찾을 수 없습니다.")
        );
        Station targetStation = stationRepository.findById(request.getTarget()).orElseThrow(
                () -> new BadRequestException("목적지 역을 찾을 수 없습니다.")
        );

        List<Line> lines = lineRepository.findAll();

        Favorite favorite = new Favorite(member.getId(), sourceStation, targetStation, lines);
        favoriteRepository.save(favorite);

        return new FavoriteResponse(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(
                () -> new BadRequestException("사용자 정보를 찾을 수 없습니다.")
        );

        return favoriteRepository.findByMemberId(member.getId()).orElseThrow()
                .stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
