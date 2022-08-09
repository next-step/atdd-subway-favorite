package nextstep.subway.applicaion;

import nextstep.auth.authentication.UserDetails;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(MemberService memberService, FavoriteRepository favoriteRepository, StationService stationService) {
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse createFavorite(UserDetails userDetails, FavoriteRequest favoriteRequest) {
        Long memberId = getMemberId(userDetails);
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(Favorite.of(memberId, source, target));
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> getFavoriteList(UserDetails userDetails) {
        Long memberId = getMemberId(userDetails);
        return FavoriteResponse.ofList(favoriteRepository.findByMemberId(memberId));
    }

    private Long getMemberId(UserDetails userDetails) {
        return memberService.findMember(userDetails.getEmail()).getId();
    }

    @Transactional
    public void deleteFavorite(UserDetails userDetails, Long favoriteId) {
        Favorite favorite = validateMemberFavorite(userDetails, favoriteId);
        favoriteRepository.delete(favorite);
    }

    private Favorite validateMemberFavorite(UserDetails userDetails, Long favoriteId) {
        Long memberId = getMemberId(userDetails);
        Favorite favorite = getFavoriteById(favoriteId);
        if (!favorite.isMembersFavorite(memberId)) {
            throw new IllegalArgumentException("사용자의 즐겨찾기가 아닙니다.");
        }
        return favorite;
    }

    private Favorite getFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId).orElseThrow(EntityNotFoundException::new);
    }

    public FavoriteResponse getFavorite(UserDetails userDetails, Long favoriteId) {
        Favorite favorite = validateMemberFavorite(userDetails, favoriteId);
        return FavoriteResponse.from(favorite);
    }
}
