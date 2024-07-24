package nextstep.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.exception.FavoritePathNotFoundException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.exception.AuthorizationException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.application.dto.PathRequest;
import nextstep.subway.path.domain.Path;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {
  private final MemberService memberService;
  private final FavoriteMapper favoriteMapper;
  private final FavoriteAppender favoriteAppender;
  private final FavoriteReader favoriteReader;
  private final FavoriteRemover favoriteRemover;
  private final PathService pathService;

  /**
   * 즐겨찾기를 생성한다.
   *
   * @param request FavoriteRequest 내용
   * @param loginMember 인증된 사용자
   * @return 생성된 즐겨찾기
   * @throws nextstep.subway.station.exception.StationNotFoundException 역을 찾을 수 없는 경우
   */
  public FavoriteResponse createFavorite(FavoriteRequest request, LoginMember loginMember) {
    if (!validatePath(request)) {
      throw new FavoritePathNotFoundException();
    }

    Member member = memberService.findMemberByEmail(loginMember.getEmail());

    Favorite favorite = Favorite.of(request.getSource(), request.getTarget(), member.getId());
    Favorite savedFavorite = favoriteAppender.append(favorite);
    return favoriteMapper.mapToFavoriteResponse(savedFavorite);
  }

  private boolean validatePath(FavoriteRequest request) {
    Path path = pathService.findPath(PathRequest.of(request.getSource(), request.getTarget()));
    return !path.getStations().isEmpty();
  }

  /**
   * 즐겨찾기 목록을 조회한다.
   *
   * @param loginMember 인증된 사용자
   * @return 즐겨찾기 목록
   */
  public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
    Member member = memberService.findMemberByEmail(loginMember.getEmail());
    List<Favorite> favorites = favoriteReader.readAllByMemberId(member.getId());
    return favorites.stream()
        .map(favoriteMapper::mapToFavoriteResponse)
        .collect(Collectors.toList());
  }

  /**
   * 즐겨찾기를 삭제한다.
   *
   * @param id 즐겨찾기 ID
   * @param loginMember 인증된 사용자
   */
  public void deleteFavorite(Long id, LoginMember loginMember) {
    Member member = memberService.findMemberByEmail(loginMember.getEmail());
    Favorite favorite = favoriteReader.readById(id);
    if (!favorite.isOwner(member.getId())) {
      throw new AuthorizationException();
    }
    favoriteRemover.removeById(id);
  }
}
