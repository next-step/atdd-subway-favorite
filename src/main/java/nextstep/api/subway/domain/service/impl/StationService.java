package nextstep.api.subway.domain.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.domain.dto.inport.StationCreateCommand;
import nextstep.api.subway.domain.model.entity.Station;
import nextstep.api.subway.infrastructure.persistence.StationRepository;
import nextstep.api.subway.interfaces.dto.response.StationResponse;
import nextstep.common.exception.subway.StationNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StationService {
	private final StationRepository stationRepository;

	@Transactional
	public StationResponse saveStation(StationCreateCommand stationCreateRequest) {
		return StationResponse.from(stationRepository.save(new Station(stationCreateRequest.getName())));
	}

	public List<StationResponse> findAllStations() {
		return stationRepository.findAll()
			.stream()
			.map(StationResponse::from)
			.collect(Collectors.toList());
	}

	public StationResponse findStation(Long stationId){
		return stationRepository.findById(stationId).map(StationResponse::from).orElseThrow(StationNotFoundException::new);
	}



	@Transactional
	public void deleteStationById(Long id) {
		stationRepository.deleteById(id);
	}

}
