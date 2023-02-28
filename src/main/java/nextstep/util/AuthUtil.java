package nextstep.util;

import nextstep.exception.AuthenticationException;
import org.springframework.util.StringUtils;

public class AuthUtil {

    public static boolean match(String header, String prefix) {
        return StringUtils.hasText(header)
                && header.startsWith(prefix);
    }

    public static String parseAccessToken(String header, String prefix) {
        if (!match(header, prefix)) {
            throw new AuthenticationException("인증 헤더 정보가 유효하지 않습니다");
        }

        return header.substring(prefix.length());
    }
}
