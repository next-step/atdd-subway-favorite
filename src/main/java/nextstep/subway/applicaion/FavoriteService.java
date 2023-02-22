package nextstep.subway.applicaion;

import nextstep.common.exception.AuthorityException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse findFavoriteResponseById(String email, Long id) {
        Favorite favorite = authExecute(email, id, this::findById);
        return createFavoriteResponse(favorite);
    }

    public List<FavoriteResponse> findFavoriteResponses(String email) {
        Member member = memberService.findByEmail(email);
        Long memberId = member.getId();
        return favoriteRepository.findByMemberId(memberId).stream()
                .filter(favorite -> favorite.isOwner(memberId))
                .map(this::createFavoriteResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FavoriteResponse save(String email, FavoriteRequest favoriteRequest) {
        Member member = memberService.findByEmail(email);
        return createFavoriteResponse(favoriteRepository.save(new Favorite(member.getId(), favoriteRequest.getSource(), favoriteRequest.getTarget())));
    }

    @Transactional
    public void delete(String email, Long id) {
        authExecute(email, id, favoriteRepository::deleteById);
    }

    private Favorite findById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        Station upStation = stationService.findById(favorite.getUpStationId());
        Station downStation = stationService.findById(favorite.getDownStationId());
        return FavoriteResponse.toResponse(favorite, upStation, downStation);
    }


    private void authExecute(String email, Long id, Consumer<Long> consumer) {
        Favorite favorite = findById(id);
        Member member = memberService.findByEmail(email);
        if (favorite.isOwner(member.getId())) {
            consumer.accept(id);
            return;
        }
        throw new AuthorityException();
    }

    private <T> T authExecute(String email, Long id, Function<Long, T> function) {
        Favorite favorite = findById(id);
        Member member = memberService.findByEmail(email);
        if (favorite.isOwner(member.getId())) {
            return function.apply(id);
        }
        throw new AuthorityException();
    }
}
