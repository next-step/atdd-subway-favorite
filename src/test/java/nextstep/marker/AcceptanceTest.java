package nextstep.marker;

import io.restassured.RestAssured;
import nextstep.utils.DataTruncateExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DataTruncateExtension.class)
@Import({AcceptanceTest.SetUpRestAssured.class})
@ActiveProfiles
public @interface AcceptanceTest {

    @AliasFor(annotation = ActiveProfiles.class, value = "profiles")
    String value() default "test";


    @TestComponent
    class SetUpRestAssured implements ApplicationListener<ServletWebServerInitializedEvent> {

        @Override
        public void onApplicationEvent(ServletWebServerInitializedEvent event) {
            RestAssured.port = event.getWebServer().getPort();
        }
    }
}
