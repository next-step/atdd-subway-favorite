package nextstep.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.dto.FavoriteCreateRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.exception.FavoriteErrorCode;
import nextstep.favorite.application.exception.FavoriteException;
import nextstep.favorite.application.exception.NotFoundFavoriteException;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.exception.MemberErrorCode;
import nextstep.member.application.exception.NotFoundMemberException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.exception.NotFoundStationException;
import nextstep.subway.applicaion.exception.SubwayErrorCode;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final MemberRepository memberRepository;

    private final StationRepository stationRepository;

    private final FavoriteRepository favoriteRepository;

    @Transactional
    public Long createFavorite(String email, FavoriteCreateRequest favoriteCreateRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        Station source = stationRepository.findById(favoriteCreateRequest.getSource())
                .orElseThrow(() -> new NotFoundStationException(SubwayErrorCode.NOT_FOUND_STATION));

        Station target = stationRepository.findById(favoriteCreateRequest.getTarget())
                .orElseThrow(() -> new NotFoundStationException(SubwayErrorCode.NOT_FOUND_STATION));

        return favoriteRepository.save(
                new Favorite(member.getId(), source, target)
        ).getId();
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        return favorites.stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void deleteFavorite(Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new NotFoundFavoriteException(FavoriteErrorCode.NOT_FOUND_FAVORITE));

        favoriteRepository.delete(favorite);
    }
}
