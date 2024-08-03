package nextstep.util;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class BaseTestSetup {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    public TransactionTemplate transactionTemplate;

    @LocalServerPort
    public int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        DatabaseCleaner.clean(applicationContext);
    }
}
