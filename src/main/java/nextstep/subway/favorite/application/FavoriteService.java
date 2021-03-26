package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.exception.FavoriteNonExistException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public Long add(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberById(memberId);
        Station source = stationService.findStationById(favoriteRequest.getSourceId());
        Station target = stationService.findStationById(favoriteRequest.getTargetId());

        Favorite favorite = new Favorite(member, source, target);
        Favorite savedFavorite = favoriteRepository.save(favorite);

        member.addFavorite(savedFavorite);
        return savedFavorite.getId();
    }

    @Transactional(readOnly = true)
    public FavoriteResponse findFavoriteResponseById(Long id) {
        Favorite favorite = findFavoriteById(id);
        return FavoriteResponse.of(favorite);
    }

    @Transactional(readOnly = true)
    public Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(FavoriteNonExistException::new);
    }
}
