package nextstep.subway.application;

import nextstep.subway.application.dto.FavouriteRequest;
import nextstep.subway.application.dto.FavouriteResponse;
import nextstep.subway.domain.favourite.FavouriteRepository;
import nextstep.subway.domain.member.Member;
import nextstep.subway.domain.station.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouriteService {
    private FavouriteRepository favouriteRepository;
    private StationRepository stationRepository;

    public FavouriteService(FavouriteRepository favouriteRepository, StationRepository stationRepository) {
        this.favouriteRepository = favouriteRepository;
        this.stationRepository = stationRepository;
    }

    public Long add(Member authenticatedMember, FavouriteRequest favouriteRequest) {
        return null;
    }

    public List<FavouriteResponse> findAll(Member authenticatedMember) {
        return null;
    }

    public void delete(Member authenticatedMember, Long favouriteId) {
    }
}
