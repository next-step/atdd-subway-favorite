package nextstep.favorite.application;

import nextstep.favorite.application.exceptions.BadRequestException;
import nextstep.favorite.application.exceptions.CannotFavoriteStationException;
import nextstep.favorite.application.exceptions.FavoriteNotFoundException;
import nextstep.member.exceptions.MemberNotFoundException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.dao.StationDao;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.auth.presentation.dto.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.path.service.PathService;
import nextstep.station.domain.Station;
import nextstep.station.presentation.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationDao stationDao;
    private PathService pathService;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            MemberRepository memberRepository,
            StationDao stationDao,
            PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationDao = stationDao;
        this.pathService = pathService;
    }


    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = getMember(loginMember.getEmail());

        Station source = stationDao.findStation(request.getSource());
        Station target = stationDao.findStation(request.getTarget());

        if (!pathService.isConnectedPath(source, target)) {
            throw new CannotFavoriteStationException("연결된 역이 아닙니다");
        }

        Favorite saved = favoriteRepository.save(new Favorite(source, target, member));
        return new FavoriteResponse(saved.getId());
    }


    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = getMember(loginMember.getEmail());
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        return favorites.stream()
                .map(favorite -> new FavoriteResponse(
                        favorite.getId(),
                        new StationResponse(favorite.getSource().getId(), favorite.getSource().getName()),
                        new StationResponse(favorite.getTarget().getId(), favorite.getTarget().getName())
                ))
                .collect(Collectors.toList());
    }


    public void deleteFavorite(LoginMember loginMember, Long id) {
        Member member = getMember(loginMember.getEmail());
        favoriteRepository.findById(id)
            .ifPresent(favorite -> {
                if (!favorite.isOwner(member)) {
                    throw new BadRequestException("다른 회원의 즐겨찾기를 삭제할 수 없어요.");
                }
                favoriteRepository.deleteByMemberAndId(member, id);
        });
    }

    public FavoriteResponse findFavorite(long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new FavoriteNotFoundException(favoriteId));

        return new FavoriteResponse(
                favorite.getId(),
                new StationResponse(favorite.getSource().getId(), favorite.getSource().getName()),
                new StationResponse(favorite.getTarget().getId(), favorite.getTarget().getName())
            );
    }


    @Transactional(readOnly = true)
    public Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));
    }
}
