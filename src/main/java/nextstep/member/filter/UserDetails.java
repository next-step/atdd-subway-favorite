package nextstep.member.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserDetails {

    private final String principal;
    private final List<String> roles;
}
