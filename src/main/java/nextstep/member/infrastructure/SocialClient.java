package nextstep.member.infrastructure;

import nextstep.member.infrastructure.dto.ProfileDto;

public interface SocialClient {
    ProfileDto getProfileFromGithub(String code);
}
