package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.domain.LoginMemberForFavorite;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.line.SectionRepository;
import nextstep.subway.path.Route;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AddFavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public AddFavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public Long addFavorite(LoginMemberForFavorite loginMember, FavoriteRequest request) {
        Member member = findMemberByEmail(loginMember.getEmail());
        Station sourceStation = findStationById(request.getSource());
        Station targetStation = findStationById(request.getTarget());

        validateExistsFavorite(member, sourceStation, targetStation);
        checkValidPathOf(sourceStation, targetStation);

        Favorite favorite = new Favorite(member.getId(), sourceStation.getId(), targetStation.getId());
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return savedFavorite.getId();
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 멤버가 존재하지 않습니다. email: " + email));
    }

    private Station findStationById(Long sourceStationId) {
        return stationRepository.findById(sourceStationId)
                .orElseThrow(() -> new IllegalArgumentException("지하철 역이 존재하지 않습니다. stationId: " + sourceStationId));
    }

    private void validateExistsFavorite(Member member, Station sourceStation, Station targetStation) {
        if (favoriteRepository.existsByMemberIdAndSourceStationIdAndTargetStationId(member.getId(), sourceStation.getId(), targetStation.getId())) {
            throw new IllegalStateException("이미 즐겨찾기에 등록된 경로입니다. member: " + member.getId() + ", source: " + sourceStation.getId() + ", target: " + targetStation.getId());
        }
    }

    private void checkValidPathOf(Station source, Station target) {
        Route route = new Route(sectionRepository.findAll());
        route.findShortestPath(source, target);
    }
}
