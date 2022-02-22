package nextstep.member.application;

import nextstep.member.domain.RequestedStation;

public interface StationQueryService {
  RequestedStation searchStation(Long id);
}
