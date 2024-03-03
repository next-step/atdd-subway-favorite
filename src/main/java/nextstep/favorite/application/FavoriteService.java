package nextstep.favorite.application;

import nextstep.exception.SubwayException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.line.Line;
import nextstep.line.LineRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.path.PathFinder;
import nextstep.station.Station;
import nextstep.station.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFinder pathFinder;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository, LineRepository lineRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = new PathFinder();
    }

    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(() -> new SubwayException("존재하지 않는 사용자입니다."));
        Station sourceStation = stationRepository.findById(request.getSource()).orElseThrow(() -> new SubwayException("존재하지 않는 역입니다."));
        Station targetStation = stationRepository.findById(request.getTarget()).orElseThrow(() -> new SubwayException("존재하지 않는 역입니다."));
        List<Line> lines = lineRepository.findAll();

        pathFinder.isValidateRoute(lines, sourceStation, targetStation);

        Favorite favorite = new Favorite(sourceStation, targetStation, member);
        return favoriteRepository.save(favorite).getId();
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(() -> new SubwayException("존재하지 않는 사용자입니다."));
        List<Favorite> favorites = favoriteRepository.findByMember(member);

        return favorites.stream().map(favorite -> new FavoriteResponse(favorite)).collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(() -> new SubwayException("존재하지 않는 사용자입니다."));
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(() -> new SubwayException("존재하지 않는 즐겨찾기입니다."));

        favorite.validateDeletionByMember(member);
        favoriteRepository.deleteById(id);
    }
}
