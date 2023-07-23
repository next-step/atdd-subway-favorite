package nextstep.utils;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {

    private static final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
    private static final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
        dataLoader.loadData();
        RestAssured.filters(requestLoggingFilter, responseLoggingFilter);
    }
}
