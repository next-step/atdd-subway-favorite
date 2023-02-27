package nextstep.subway.applicaion;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public Long addFavorite(String email, Long source, Long target) {
        Member member = memberService.findMemberByEmail(email);

        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        return favoriteRepository.save(new Favorite(member.getId(), sourceStation.getId(), targetStation.getId())).getId();
    }

    public List<FavoriteResponse> findAll(String email) {
        Member member = memberService.findMemberByEmail(email);

        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());

        return favorites.stream()
            .map(favorite -> {
                Station source = stationService.findById(favorite.getSourceId());
                Station target = stationService.findById(favorite.getTargetId());
                return FavoriteResponse.of(favorite, source, target);
            })
            .collect(toList());
    }

    @Transactional
    public void deleteFavorite(String email, Long favoriteId) {
        Member member = memberService.findMemberByEmail(email);
        favoriteRepository.deleteByIdAndMemberId(favoriteId, member.getId());
    }
}
