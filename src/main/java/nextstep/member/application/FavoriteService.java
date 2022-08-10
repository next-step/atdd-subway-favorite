package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteResponse saveFavorite(String username, FavoriteRequest request) {
        Member member = memberRepository.findByEmail(username).orElseThrow(IllegalArgumentException::new);
        Station target = stationRepository.findById(request.getTarget()).orElseThrow(IllegalArgumentException::new);
        Station source = stationRepository.findById(request.getSource()).orElseThrow(IllegalArgumentException::new);

        Favorite saveFavorite = favoriteRepository.save(Favorite.of(member, target.getId(), source.getId()));
        return FavoriteResponse.of(saveFavorite.getId(), target, source);
    }

    public FavoriteResponse findById(Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Station target = stationRepository.findById(favorite.getTargetId()).orElseThrow(IllegalArgumentException::new);
        Station source = stationRepository.findById(favorite.getSourceId()).orElseThrow(IllegalArgumentException::new);
        return FavoriteResponse.of(id, target, source);
    }

}
