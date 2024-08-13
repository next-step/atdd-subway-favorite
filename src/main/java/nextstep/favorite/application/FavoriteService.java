package nextstep.favorite.application;

import static nextstep.global.exception.ExceptionCode.NOT_FOUND_MEMBER;

import java.util.Optional;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.Favorite.Builder;
import nextstep.favorite.infrastructrue.FavoriteJpaRepository;
import nextstep.favorite.infrastructrue.FavoriteRepository;
import nextstep.global.exception.CustomException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * TODO: LoginMember 를 추가로 받아서 FavoriteRequest 내용과 함께 Favorite 를 생성합니다.
     *
     * @param request
     */
    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberRepository.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

        Favorite favorite = new Builder().memberId(member.getId())
                .sourceStationId(request.getSource())
                .targetStationId(request.getTarget())
                .build();

        Favorite newFavorite = favoriteRepository.save(favorite);
        return newFavorite.getId();
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return null;
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
