package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NotDeleteFavoriteException;
import nextstep.subway.exception.NotFoundFavoriteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public FavoriteResponse save(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMember(loginMember.getEmail());

        Station sourceStation = stationService.findById(favoriteRequest.getSource());
        Station targetStation = stationService.findById(favoriteRequest.getTarget());

        Favorite favorite = Favorite.create(member.getId(), sourceStation, targetStation);
        favoriteRepository.save(favorite);
        return FavoriteResponse.from(favorite);
    }

    public FavoriteResponse findById(LoginMember loginMember, Long id) {
        Member member = memberService.findMember(loginMember.getEmail());
        return favoriteRepository.findByIdAndMemberId(id, member.getId())
                .map(FavoriteResponse::from)
                .orElse(FavoriteResponse.empty());
    }

    public List<FavoriteResponse> findAll(LoginMember loginMember) {
        Member member = memberService.findMember(loginMember.getEmail());
        return favoriteRepository.findAllByMemberId(member.getId())
                .stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void delete(LoginMember loginMember, Long id) {
        Member member = memberService.findMember(loginMember.getEmail());
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(NotFoundFavoriteException::new);
        if (favorite.isNotOwner(member)) {
            throw new NotDeleteFavoriteException(member.getId());
        }
        favoriteRepository.delete(favorite);
    }
}
