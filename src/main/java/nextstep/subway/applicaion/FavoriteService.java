package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.abstractive.MemberProvider;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteReadResponse;
import nextstep.subway.applicaion.message.Message;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Favorite createFavorite(MemberProvider memberProvider, FavoriteCreateRequest request) {
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        if (source.equals(target)) {
            throw new IllegalArgumentException(Message.SOURCE_TARGET_DUPLICATE_STATION.getMessage());
        }
        Member member = memberRepository.findByEmail(memberProvider.getPrincipal())
                .orElseThrow(() -> new IllegalArgumentException(Message.MEMBER_NOT_EXIST.getMessage()));
        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));
        return favorite;
    }

    @Transactional(readOnly = true)
    public List<FavoriteReadResponse> readFavorites(MemberProvider memberProvider) {
        Member member = memberRepository.findByEmail(memberProvider.getPrincipal())
                .orElseThrow(() -> new IllegalArgumentException(Message.MEMBER_NOT_EXIST.getMessage()));
        return favoriteRepository.findByMember(member)
                .stream()
                .map(FavoriteReadResponse::new)
                .collect(toList());
    }

    @Transactional
    public void deleteFavorite(MemberProvider memberProvider, Long favoriteId) {
        String email = memberProvider.getPrincipal();
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException(Message.INVALID_FAVORITE_ID.getMessage()));
        if (!favorite.isThisYourFavorite(email)) {
            throw new IllegalArgumentException(Message.UNAUTHORIZED_FAVORITE.getMessage());
        }
        favoriteRepository.deleteById(favoriteId);
    }

}
