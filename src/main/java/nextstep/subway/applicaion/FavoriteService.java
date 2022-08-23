package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveFavorite(LoginMember loginMember, FavoriteRequest request) {
        Station source = stationRepository.findById(request.getSource()).orElseThrow(IllegalArgumentException::new);
        Station target = stationRepository.findById(request.getTarget()).orElseThrow(IllegalArgumentException::new);
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(IllegalArgumentException::new);
        return favoriteRepository.save(Favorite.of(source.getId(), target.getId(), member.getId())).getId();
    }

    public List<FavoriteResponse> findFavorites() {
        return favoriteRepository.findAll()
                .stream()
                .map(this::createFavoriteResponse)
                .collect(Collectors.toList());
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        Station source = stationRepository.findById(favorite.getSourceId()).orElseThrow(IllegalArgumentException::new);
        Station target = stationRepository.findById(favorite.getTargetId()).orElseThrow(IllegalArgumentException::new);
        return FavoriteResponse.of(favorite.getId(), source, target);
    }

    @Transactional
    public void deleteLine(Long id) {
        favoriteRepository.deleteById(id);
    }
}
