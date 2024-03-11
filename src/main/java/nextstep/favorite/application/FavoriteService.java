package nextstep.favorite.application;

import nextstep.exception.BadRequestException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.exception.AuthenticationException;
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
        Member member = findMember(loginMember.getEmail());
        Station sourceStation = findStation(request.getSource());
        Station targetStation = findStation(request.getTarget());

        List<Line> lines = lineRepository.findAll();

        Favorite favorite = new Favorite(member.getId(), sourceStation, targetStation, lines);
        favoriteRepository.save(favorite);

        return new FavoriteResponse(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = findMember(loginMember.getEmail());

        return favoriteRepository.findByMemberId(member.getId())
                .orElseThrow()
                .stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Member member = findMember(loginMember.getEmail());

        favoriteRepository.findByIdAndMemberId(favoriteId, member.getId()).orElseThrow(
                () -> new AuthenticationException("즐겨찾기를 등록한 회원 정보와 일치하지 않습니다.")
        );

        favoriteRepository.deleteById(favoriteId);
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(
                () -> new BadRequestException("역을 찾을 수 없습니다.")
        );
    }

    private Member findMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new BadRequestException("사용자 정보를 찾을 수 없습니다.")
        );
    }
}
