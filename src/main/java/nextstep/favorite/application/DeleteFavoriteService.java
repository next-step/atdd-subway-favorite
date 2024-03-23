package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.domain.LoginMemberForFavorite;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DeleteFavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;

    public DeleteFavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
    }

    public void deleteFavorite(LoginMemberForFavorite loginMember, Long id) {
        Member member = findMemberByEmail(loginMember.getEmail());
        Favorite favorite = findFavoriteById(id);
        favorite.validateOwner(member);
        favoriteRepository.delete(favorite);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 멤버가 존재하지 않습니다. email: " + email));
    }

    private Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID 에 해당하는 즐겨찾기가 존재하지 않습니다. id: " + id));
    }
}
