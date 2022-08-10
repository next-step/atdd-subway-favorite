package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.FavoriteDto;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.ui.exception.FavoriteOwnerException;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteProducer {
    private final MemberRepository memberRepository;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteDto saveFavorite(String username, FavoriteRequest request) {
        Member member = memberRepository.findByEmail(username).orElseThrow(IllegalArgumentException::new);
        Station target = stationService.findById(request.getTarget());
        Station source = stationService.findById(request.getSource());

        Favorite saveFavorite = favoriteRepository.save(Favorite.of(member, target.getId(), source.getId()));
        return FavoriteDto.of(saveFavorite.getId(), target, source);
    }

    public FavoriteDto findFavorite(Long id) {
        Favorite favorite = findById(id);
        Station target = stationService.findById(favorite.getTargetId());
        Station source = stationService.findById(favorite.getSourceId());
        return FavoriteDto.of(id, target, source);
    }

    private Favorite findById(Long id) {
        return favoriteRepository.findById(id).orElseThrow(
                () -> new FavoriteOwnerException("즐겨찾기를 찾을 수 없습니다."));
    }

    public void deleteFavorite(Long id) {
        checkExistsFavorite(id);
        favoriteRepository.deleteById(id);
    }

    private void checkExistsFavorite(Long id) {
        if (!favoriteRepository.existsById(id)) {
            throw new FavoriteOwnerException("즐겨찾기를 찾을 수 없습니다.");
        }
    }
}
