package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.AuthServices;
import nextstep.member.domain.Member;
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


    public Favorite save(FavoriteCreateRequest request, String header) {
        Member member = authServices.findAuth(header).findMember(header);
        Favorite favorite = request.toEntity(stationRepository::findById, member); // request에서 station id를 꺼내서 조회하는게 좋을까?
        return favoriteRepository.save(favorite);
    }

    public FavoriteResponse findById(Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기 조회 실패 id:" + id));

        return FavoriteResponse.of(favorite);
    }

    public void deleteById(Long id) {
        favoriteRepository.deleteById(id);
    }
}
