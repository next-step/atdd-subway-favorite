package nextstep.member.application;

import nextstep.member.application.dto.CreateFavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationService stationService;
    private MemberRepository memberRepository;
    private LineService lineService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberRepository memberRepository, LineService lineService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
        this.lineService = lineService;
    }

    public Favorite createFavorite(CreateFavoriteRequest request, String email) {
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        if (subwayMap.findPath(source, target) == null) {
            throw new IllegalArgumentException();
        }

        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return favoriteRepository.save(new Favorite(member, source, target));
    }

    public FavoriteResponse findFavorite(Long id, String email) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        favorite.checkOwner(member);
        return FavoriteResponse.of(favorite);
    }

    public void deleteFavorite(Long id, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
        favorite.checkOwner(member);
        favoriteRepository.delete(favorite);
    }
}
