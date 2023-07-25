package nextstep.member.application;

import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository
            , StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse createFavorites(FavoriteRequest request, UserPrincipal userPrincipal) {
        Member member = memberRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException());
        Station source = stationRepository.findById(request.getSource())
                .orElseThrow(() -> new RuntimeException());
        Station target = stationRepository.findById(request.getTarget())
                .orElseThrow(() -> new RuntimeException());
        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member.getId()));

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> selectFavorites(UserPrincipal userPrincipal) {
        Member member = memberRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException());
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        return FavoriteResponse.of(favorites);
    }

    @Transactional
    public void deleteFavorites(Long id, UserPrincipal userPrincipal) {
        Member member = memberRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException());
        favoriteRepository.deleteByIdAndMemberId(id, member.getId());
    }
}
