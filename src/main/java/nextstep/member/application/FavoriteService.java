package nextstep.member.application;

import nextstep.auth.BadRequestException;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;


    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository,
            MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }


    @Transactional
    public long create(String email, long source, long target) {
        Station sourceStation = stationRepository.findById(source).orElseThrow(BadRequestException::new);
        Station tartgetStation = stationRepository.findById(target).orElseThrow(BadRequestException::new);
        Favorite favorite = new Favorite(sourceStation, tartgetStation);
        favoriteRepository.save(favorite);
        Member member = memberRepository.findByEmail(email).orElseThrow(BadRequestException::new);
        member.addFavorite(favorite);
        return favorite.getId();
    }

    @Transactional
    public void delete(String email, Long id) {
        Member member = memberRepository.findByEmail(email).orElseThrow(BadRequestException::new);
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(BadRequestException::new);
        member.delete(favorite);
        favoriteRepository.deleteById(id);
    }
}
