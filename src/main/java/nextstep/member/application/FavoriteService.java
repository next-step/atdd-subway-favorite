package nextstep.member.application;

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
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }


    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Station upStation = findStationById(request.getSource());
        Station downStation = findStationById(request.getTarget());
        Member member = findMemberById(memberId);
        Favorite favorite = favoriteRepository.save(new Favorite(upStation, downStation, member));
        return FavoriteResponse.of(favorite);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = findMemberById(memberId);
        List<Favorite> favorites = favoriteRepository.findAllByMember(member);
        return favorites
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        if (isNotMine(memberId, favoriteId)) {
            throw new IllegalArgumentException();
        }
        favoriteRepository.deleteById(favoriteId);
    }

    private boolean isNotMine(Long memberId, Long favoriteId) {
        List<Favorite> favorites = favoriteRepository.findAllByMember(findMemberById(memberId));
        return favorites.stream()
                .noneMatch(f -> f.getId().equals(favoriteId));
    }
}
