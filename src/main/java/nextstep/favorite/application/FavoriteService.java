package nextstep.favorite.application;

import nextstep.auth.principal.UserPrincipal;
import nextstep.exception.AuthenticationException;
import nextstep.exception.StationNotFoundException;
import nextstep.favorite.application.request.FavoriteCreateRequest;
import nextstep.favorite.application.response.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.line.domain.LineRepository;
import nextstep.line.domain.PathFinder;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;
    private final LineRepository lineRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationRepository stationRepository, MemberRepository memberRepository, LineRepository lineRepository, FavoriteRepository favoriteRepository) {
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
        this.lineRepository = lineRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Favorite createFavorite(UserPrincipal userPrincipal, FavoriteCreateRequest favoriteCreateRequest) {
        Station source = findStation(favoriteCreateRequest.getSource());
        Station target = findStation(favoriteCreateRequest.getTarget());
        Member member = findMemberByEmail(userPrincipal.getUsername());
        PathFinder pathFinder = new PathFinder(lineRepository.findAll());

        Favorite favorite = new Favorite(member.getId(), source, target, pathFinder);
        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(UserPrincipal userPrincipal) {
        Member member = findMemberByEmail(userPrincipal.getUsername());
        return favoriteRepository.findFavoritesByMemberId(member.getId())
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long id, UserPrincipal userPrincipal) {
        Favorite favorite = findFavorite(id);
        Member member = findMemberByEmail(userPrincipal.getUsername());
        if (!favorite.isOwner(member)) {
            throw new AuthenticationException();
        }
        favoriteRepository.delete(favorite);
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(StationNotFoundException::new);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    private Favorite findFavorite(Long id) {
        return favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
    }

}
