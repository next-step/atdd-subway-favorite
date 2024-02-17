package nextstep.api.subway.domain.service;

import java.util.List;

import nextstep.api.subway.domain.dto.inport.LineCreateCommand;
import nextstep.api.subway.domain.dto.inport.LineUpdateCommand;
import nextstep.api.subway.interfaces.dto.response.LineResponse;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface LineService {
	LineResponse saveLine(LineCreateCommand createRequest);

	List<LineResponse> findAllLines();

	LineResponse findLineById(Long id);

	LineResponse updateLineById(Long id, LineUpdateCommand updateRequest);

	void deleteLineById(Long id);

	boolean isProperSectionExist(Long source, Long target);
}
