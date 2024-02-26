package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private PathService pathService;


    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.pathService = pathService;
    }

    @Transactional
    public Favorite createFavorite(FavoriteRequest request, LoginMember loginMember) {
        Member member = getMember(loginMember);
        Station source = this.stationRepository.findById(request.getSource()).orElseThrow(() -> new RuntimeException("존재하지 않는 출발역입니다."));
        Station target = this.stationRepository.findById(request.getTarget()).orElseThrow(() -> new RuntimeException("존재하지 않는 도착역입니다."));

        PathResponse path = this.pathService.findPath(source.getId(), target.getId());
        if(path.hasNoPath()){
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
        return favoriteRepository.save(new Favorite(member, source, target));
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = getMember(loginMember);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long id, LoginMember loginMember) {
        Member member = getMember(loginMember);
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 즐겨찾기입니다."));
        if (!favorite.isOwner(member)) {
            throw new AuthenticationException();
        }
        favoriteRepository.deleteById(id);
    }

    private Member getMember(LoginMember loginMember) {
        return this.memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}
