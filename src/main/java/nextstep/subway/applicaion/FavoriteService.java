package nextstep.subway.applicaion;

import nextstep.exception.CantDeleteFavoriteException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.UserDetails;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository,
                           FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorites(UserDetails userDetails, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(userDetails.getId()).orElseThrow(EntityNotFoundException::new);
        Station source = stationRepository.findById(favoriteRequest.getSourceId())
                .orElseThrow(EntityNotFoundException::new);
        Station target = stationRepository.findById(favoriteRequest.getTargetId())
                .orElseThrow(EntityNotFoundException::new);

        Favorite favorite = favoriteRepository.save(new Favorite());
        favorite.setSource(source);
        favorite.setTarget(target);

        member.addFavorite(favorite);
        return new FavoriteResponse(favorite);
    }

    public void deleteFavorite(UserDetails userDetails, Long id) {
        Member member = memberRepository.findById(userDetails.getId()).orElseThrow(EntityNotFoundException::new);
        canDeleteFavorite(member, id);
        Favorite favorite = favoriteRepository.getById(id);
        member.getFavorites().getFavorites().remove(favorite);
    }

    public void canDeleteFavorite(Member member, Long favoriteId) {
        if (!member.isFavoriteOwner(favoriteId)) {
            throw new CantDeleteFavoriteException();
        }
    }

    public List<FavoriteResponse> getFavorites(UserDetails userDetails) {
        Member member = memberRepository.findById(userDetails.getId()).orElseThrow(EntityNotFoundException::new);
        return member.getFavorites().getFavorites().stream()
                .map(favorite -> new FavoriteResponse(favorite))
                .collect(Collectors.toList());
    }
}
