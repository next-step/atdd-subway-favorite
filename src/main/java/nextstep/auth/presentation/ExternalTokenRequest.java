package nextstep.auth.presentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExternalTokenRequest {
    private final String code;
}
