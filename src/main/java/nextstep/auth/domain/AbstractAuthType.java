package nextstep.auth.domain;

import org.springframework.util.StringUtils;

public abstract class AbstractAuthType implements AuthType {

    public boolean match(String header) {
        if (StringUtils.hasText(header)
                && header.startsWith(getPrefix())) {
            return true;
        }

        return false;
    }

    public String parseAccessToken(String header) {
        if (!match(header)) {
            throw new IllegalArgumentException("인증 헤더 정보가 유효하지 않습니다");
        }

        return header.substring(getPrefix().length());
    }

    protected abstract String getPrefix();
}
