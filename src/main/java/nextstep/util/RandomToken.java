package nextstep.util;

import java.util.Random;

public class RandomToken {

    public static String createRandomToken(int length) {
        String token = "";

        for (int i = 0; i < length; i++) {
            boolean typeNum = new Random().nextBoolean();

            if (typeNum) {
                token = token + makeRamdomNum();
                continue;
            }

            token = token + makeRamdomLowerChar();
        }

        return token;
    }

    private static char makeRamdomLowerChar() {
        return (char)(new Random().nextInt(26)+97);
    }

    private static String makeRamdomNum() {
        return String.valueOf(new Random().nextInt(9));
    }
}
