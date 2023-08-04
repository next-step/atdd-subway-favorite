package nextstep.utils;

public class AuthorizationHeader {
    public static final String DELIMITER = " ";

    private AuthorizationHeader() {
    }

    public static String findAccessToken(String header) {
        int beginIndex = header.indexOf(DELIMITER) + 1;
        return header.substring(beginIndex);
    }
}
