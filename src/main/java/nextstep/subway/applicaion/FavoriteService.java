package nextstep.subway.applicaion;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public List<FavoriteResponse> getFavorites(String email) {
        Member member = findMemberByEmail(email);

        return member.getFavorites()
                .stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public FavoriteResponse saveFavorites(String email, FavoriteRequest request) {
        Member member = findMemberByEmail(email);

        Station source = stationRepository.findById(request.getSource())
                .orElseThrow(() -> new IllegalArgumentException("Source Station이 존재하지 않습니다."));
        Station target = stationRepository.findById(request.getTarget())
                .orElseThrow(() -> new IllegalArgumentException("Target Station이 존재하지 않습니다."));

        Favorite favorite = new Favorite(source, target, member);
        member.getFavorites()
                .add(favorite);
        Member saveMember = memberRepository.save(member);

        Favorite saveFavorite = saveMember.getFavorites()
                .get(saveMember.getFavorites()
                        .size() - 1);
        return FavoriteResponse.from(saveFavorite);
    }

    @Transactional
    public void deleteFavorite(String email, Long favoriteId) {
        Member member = findMemberByEmail(email);
        Favorite favorite = member.getFavorites()
                .stream()
                .filter(fa -> favoriteId.equals(fa.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Favorite 이 존재하지 않습니다."));
        member.getFavorites()
                .remove(favorite);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("존재하는 사용자가 아닙니다."));
    }
}
