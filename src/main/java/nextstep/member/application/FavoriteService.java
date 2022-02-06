package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.FavoriteNotOwningException;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
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

	@Transactional(readOnly = true)
	public List<FavoriteResponse> findByMemberId(long memberId) {
		Member member = findMember(memberId);
		return FavoriteResponse.ofList(member.getFavorites());
	}

	public FavoriteResponse save(long memberId, FavoriteRequest request) {
		Station sourceStation = findStation(request.getSource());
		Station targetStation = findStation(request.getTarget());
		Member member = findMember(memberId);

		Favorite favorite = new Favorite(sourceStation, targetStation, member);
		favorite.setMember(member);
		favoriteRepository.save(favorite);

		return FavoriteResponse.of(favorite);
	}

	private Member findMember(long memberId) {
		return memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
	}

	private Station findStation(long stationId) {
		return stationRepository.findById(stationId).orElseThrow(EntityNotFoundException::new);
	}

	public void removeById(long favoriteId, long memberId) {
		validateMemberHasFavoriteOrElseThrow(memberId, favoriteId);
		favoriteRepository.deleteById(favoriteId);
	}

	private void validateMemberHasFavoriteOrElseThrow(long memberId, long favoriteId) {
		Member member = findMember(memberId);
		Favorite favorite = findById(favoriteId);

		if (!favorite.isMemberEqual(member)) {
			throw new FavoriteNotOwningException();
		}
	}

	private Favorite findById(long favoriteId) {
		return favoriteRepository.findById(favoriteId).orElseThrow(EntityNotFoundException::new);
	}
}
