package nextstep.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.dto.FavoriteCreateRequest;
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
}
