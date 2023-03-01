package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.AuthServices;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final AuthServices authServices;


    public Favorite save(FavoriteCreateRequest request) {
        Favorite favorite = request.toEntity(stationRepository::findById); // request에서 station id를 꺼내서 조회하는게 좋을까?
        return favoriteRepository.save(favorite);
    }

    public FavoriteResponse findByMemberId(Long memberId) {
        Favorite favorite = favoriteRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기 조회 실패 id:" + memberId));

        // boolean으로 검증값을 리턴해서 클라이언트 코드에서 exception을 던지는게 좋을까?
        // 메서드 내부에서 exception을 직접던지면 exeption 클래스와 메시지가 캡슈화해서 클라이언트에서 더 사용하기 좋을까?
        // exception 클래스와, 메시지를 유연하게 가져가기 위해 클라이언트쪽에서 던지는게 좋을까?

        return FavoriteResponse.of(favorite);
    }

    public void deleteMyFavoriteById(Long id, Long memberId) {
        // 이렇게 호출하면 불필요하게 response 객체를 한번더 만든다.
        // 조회 ~ 검증 로직까지 처리하는 메서드를 추가하는게 좋을까? 아니면 response객체를 컨트롤러에서 생성하도록 변경하는게 좋을까?
        // 아니면 객체하나 정도 생성하는 정도는 무시하고 이대로하는게 좋을까?
        findByMemberId(id);

        favoriteRepository.deleteById(id);
    }
}
