package nextstep.subway.favorite.application;

import nextstep.subway.exceptions.NotFoundStationException;
import nextstep.subway.exceptions.NotFoundUserException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }


    public FavoriteResponse saveFavorites(Long memberId, FavoriteRequest request) {
        Member member = findMemberById(memberId);
        Station source = findStationById(request.getSource());
        Station target = findStationById(request.getTarget());
        Favorite favorite = new Favorite(member, source, target);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        member.addFavorite(savedFavorite);


        return FavoriteResponse.of(savedFavorite);
    }

    public List<FavoriteResponse> findFavoritesAllByMemberId(Long memberId) {
        Member member = findMemberById(memberId);
        return toFavoriteResponse(member);
    }

    private static List<FavoriteResponse> toFavoriteResponse(Member member) {
        List<FavoriteResponse> list = new ArrayList<>();
        member.forEachFromFavorite(favorite -> list.add(FavoriteResponse.of(favorite)));
        return list;
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = findMemberById(memberId);
        member.deleteFavorite(favoriteId);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(NotFoundStationException::new);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(NotFoundUserException::new);
    }
}
