package nextstep.favorite.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {
  private final FavoriteRepository favoriteRepository;
  private final MemberService memberService;
  private final FavoriteMapper favoriteMapper;

  /**
   * 즐겨찾기를 생성한다.
   *
   * @param request FavoriteRequest 내용
   * @param loginMember 인증된 사용자
   * @throws nextstep.subway.station.exception.StationNotFoundException 역을 찾을 수 없는 경우
   */
  public FavoriteResponse createFavorite(FavoriteRequest request, LoginMember loginMember) {
    Member member = memberService.findMemberByEmail(loginMember.getEmail());
    Favorite favorite = Favorite.of(request.getSource(), request.getTarget(), member.getId());
    Favorite savedFavorite = favoriteRepository.save(favorite);
    return favoriteMapper.mapToFavoriteResponse(savedFavorite);
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
   *
   * @param id
   */
  public void deleteFavorite(Long id) {
    favoriteRepository.deleteById(id);
  }
}
