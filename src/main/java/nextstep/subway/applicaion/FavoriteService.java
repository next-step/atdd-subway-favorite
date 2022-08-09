package nextstep.subway.applicaion;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.member.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.FavoritesResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = getMember(loginMember);
        Station source = getStation(request.getSource());
        Station target = getStation(request.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(member.getId(), source, target));
        return new FavoriteResponse(favorite.getId(), getStationResponse(favorite.getSource()), getStationResponse(favorite.getTarget()));
    }


    @Transactional(readOnly = true)
    public FavoritesResponse findFavorites(LoginMember loginMember) {
        Member member = getMember(loginMember);

        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return new FavoritesResponse(
            favorites.stream().map(
                favorite -> new FavoriteResponse(favorite.getId(), getStationResponse(favorite.getSource()), getStationResponse(favorite.getTarget()))
            ).collect(Collectors.toList())
        );
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, long id) {
        Member member = getMember(loginMember);

        Favorite favorite = favoriteRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (!favorite.getMemberId().equals(member.getId())) {
            throw new AuthenticationException();
        }

        favoriteRepository.delete(favorite);
    }

    private Member getMember(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(IllegalArgumentException::new);
    }

    private Station getStation(long id) {
        return stationRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    private StationResponse getStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
