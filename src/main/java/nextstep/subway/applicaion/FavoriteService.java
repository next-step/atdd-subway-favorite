package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;

    private final StationRepository stationRepository;

    @Transactional
    public FavoriteResponse createFavorite(String email, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);

        Station sourceStation = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(IllegalArgumentException::new);
        Station targetStation = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(IllegalArgumentException::new);

        Favorite favorite = favoriteRepository.save(Favorite.builder()
                .source(sourceStation)
                .target(targetStation)
                .member(member)
                .build());

        return FavoriteResponse.of(favorite);
    }


    public List<FavoriteResponse> getFavorites(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);

        return member.getFavorites().stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }


    @Transactional
    public void deleteFavorite(String email, Long id) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);

        Favorite favorite = favoriteRepository.findByIdAndMember(id, member)
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기를 찾을 수 없어요"));

        favoriteRepository.delete(favorite);
    }

}
