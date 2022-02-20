package nextstep.subway.applicaion;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		Station source = stationRepository.getById(favoriteRequest.getSourceId());
		Station target = stationRepository.getById(favoriteRequest.getTargetId());

		Favorite favorite = favoriteRepository.save(new Favorite());
		favorite.setSource(source);
		favorite.setTarget(target);

		member.addFavorite(favorite);
		System.out.println("=================================");
		System.out.println(member.getId());
		System.out.println(member.getFavorites().getFavorites().get(0).getId());
		System.out.println(member.getFavorites().getFavorites().get(0).getSource().getId());
		System.out.println(member.getFavorites().getFavorites().get(0).getTarget().getId());
		System.out.println("=================================");
		return new FavoriteResponse(favorite);
	}

	public void deleteFavorite(UserDetails userDetails, Long id) {
		Member member = memberRepository.findById(userDetails.getId()).orElseThrow(EntityNotFoundException::new);
		canDeleteFavorite(member, id);
		favoriteRepository.deleteById(id);
	}

	public void canDeleteFavorite(Member member, Long favoriteId) {
		if (!member.isFavoriteOwner(favoriteId)) {
			throw new CantDeleteFavoriteException();
		}
	}
}
