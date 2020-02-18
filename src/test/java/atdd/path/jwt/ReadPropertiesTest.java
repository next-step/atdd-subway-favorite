package atdd.path.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReadPropertiesTest {
    @Autowired
    private Properties properties;

    @Test
    public void properties의_시크키릿_값이_들어온다() {
        String key = properties.getSecretKey();
        assertEquals("secretkey", key);
    }

    @Test
    public void properties의_유효기간_값이_들어온다() {
        int expireLength = properties.getExpireLength();
        assertEquals(10000, expireLength);
    }
}
