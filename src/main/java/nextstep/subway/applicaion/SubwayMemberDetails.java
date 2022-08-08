package nextstep.subway.applicaion;

import org.springframework.stereotype.Service;

import nextstep.subway.domain.SubwayMember;

@Service
public interface SubwayMemberDetails {
	SubwayMember getMemberIdByEmail(String email);
}
