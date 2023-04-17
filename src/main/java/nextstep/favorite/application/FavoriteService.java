package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.dto.FavoriteRequestDto;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public Favorite create(String email, FavoriteRequestDto favoriteRequestDto) {
        Member member = findMember(email);
        Station sourceStation = stationService.findById(favoriteRequestDto.getSource());
        Station targetStation = stationService.findById(favoriteRequestDto.getTarget());
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        return favoriteRepository.save(favorite);
    }

    @Transactional(readOnly = true)
    public List<Favorite> getList(String email) {
        Member member = findMember(email);
        return favoriteRepository.findAllByMember(member);
    }

    private Member findMember(String email) {
        return memberService.findByEmail(email);
    }



    @Transactional
    public void deleteById(String email, Long id) {
        Member member = findMember(email);
        if(existsByIdAndMember(id, member)){
            favoriteRepository.deleteById(id);
        }
    }

    private boolean existsByIdAndMember(Long id, Member member) {
        return favoriteRepository.existsByIdAndMember(id, member);
    }
}
