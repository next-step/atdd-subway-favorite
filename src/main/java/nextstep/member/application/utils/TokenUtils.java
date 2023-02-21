package nextstep.member.application.utils;

public class TokenUtils {

    public static String parseToken(String authorization) {
        return authorization.split(" ")[1];
    }
}
