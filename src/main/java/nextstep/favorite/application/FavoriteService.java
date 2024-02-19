package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
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


    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    /**
     * TODO: LoginMember 를 추가로 받아서 FavoriteRequest 내용과 함께 Favorite 를 생성합니다.
     * @param request, loginMember
     */
    @Transactional
    public void createFavorite(FavoriteRequest request, LoginMember loginMember) {
        Member member = getMember(loginMember);
        Station source = this.stationRepository.findById(request.getSource()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출발역입니다."));
        Station target = this.stationRepository.findById(request.getTarget()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도착역입니다."));
        favoriteRepository.save(new Favorite(member, source, target));
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = getMember(loginMember);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */

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
