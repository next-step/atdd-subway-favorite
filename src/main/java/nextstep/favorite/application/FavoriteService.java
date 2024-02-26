package nextstep.favorite.application;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.auth.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.ui.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;
    private final SectionService sectionService;

    public Long createFavorite(final String email, final FavoriteRequest request) {
        final var member = memberService.findMemberOrElseThrow(
            email,
            () -> new AuthenticationException("유효한 인증 토큰이 아닙니다.")
        );

        final var source = stationService.findById(request.getSource());
        final var target = stationService.findById(request.getTarget());

        final var sections = sectionService.findAll();

        final var favorite = new Favorite(member, source, target, sections);
        return favoriteRepository.save(favorite).getId();
    }

    public List<FavoriteResponse> findFavoritesByMemberEmail(final String email) {
        final var member = memberService.findMemberOrElseThrow(
            email,
            () -> new AuthenticationException("유효한 인증 토큰이 아닙니다.")
        );

        return favoriteRepository.findAllByMember(member).stream()
            .map(favorite ->
                FavoriteResponse.from(
                    favorite,
                    StationResponse.from(favorite.getSource()),
                    StationResponse.from(favorite.getTarget())
                )
            )
            .collect(Collectors.toList());
    }

    public FavoriteResponse findFavoriteByMemberEmail(final String email, final Long favoriteId) {
        final var member = memberService.findMemberOrElseThrow(
            email,
            () -> new AuthenticationException("유효한 인증 토큰이 아닙니다.")
        );

        return favoriteRepository.findByIdAndMember(favoriteId, member)
            .map(favorite ->
                FavoriteResponse.from(
                    favorite,
                    StationResponse.from(favorite.getSource()),
                    StationResponse.from(favorite.getTarget())
                )
            )
            .orElseThrow(() -> new BusinessException("즐겨찾기 정보를 찾을 수 없습니다."));
    }

    public void deleteFavorite(final String email, final Long id) {
        final var member = memberService.findMemberOrElseThrow(
            email,
            () -> new AuthenticationException("유효한 인증 토큰이 아닙니다.")
        );

        favoriteRepository.findByIdAndMember(id, member)
            .ifPresentOrElse(
                favoriteRepository::delete,
                () -> {throw new BusinessException("즐겨찾기 정보를 찾을 수 없습니다.");}
            );
    }
}
