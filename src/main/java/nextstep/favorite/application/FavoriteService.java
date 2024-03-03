package nextstep.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public void createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Station source = stationRepository.findById(request.getSource()).orElseThrow();
        Station target = stationRepository.findById(request.getTarget()).orElseThrow();
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow();
        Favorite favorite = Favorite.of(member.getId(), source, target);
        favoriteRepository.save(favorite);
    }


    public List<FavoriteResponse> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return favorites.stream()
            .map(FavoriteResponse::new)
            .collect(Collectors.toList());
    }


    @Transactional
    public void deleteFavorite(Long id) {

        favoriteRepository.deleteById(id);
    }
}
