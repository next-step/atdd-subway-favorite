package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteValidationService;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteValidationService favoriteValidationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService,
                           StationService stationService,
                           FavoriteValidationService favoriteValidationService,
                           FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteValidationService = favoriteValidationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(String email, FavoriteRequest request) {
        Long memberId = memberService.findMember(email).getId();
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        favoriteValidationService.validateDuplicate(memberId, source.getId(), target.getId());

        Favorite favorite = request.toEntity();
        favorite.toMember(memberId);
        favoriteRepository.save(favorite);
        return new FavoriteResponse(favorite, source, target);
    }
}
