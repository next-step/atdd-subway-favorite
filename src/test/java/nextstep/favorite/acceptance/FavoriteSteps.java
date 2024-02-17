package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.favorite.application.dto.FavoriteCreateRequest;
import org.springframework.http.MediaType;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

public class FavoriteSteps {

    private static final String PREFIX_PATH = "/favorites";
    
    public static FavoriteRequestBuilder 즐겨찾기_요청을_구성한다() {
        return new FavoriteSteps().new FavoriteRequestBuilder();
    }

    public class FavoriteRequestBuilder {
        private RequestSpecification spec;
        private String accessToken;
        private FavoriteCreateRequest body;
        private int statusCode = OK.value();

        public FavoriteRequestBuilder() {
            this.spec = RestAssured.given().log().all();
        }

        public FavoriteRequestBuilder 로그인을_한다(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public FavoriteRequestBuilder Response_HTTP_상태_코드(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public FavoriteRequestBuilder 즐겨찾기_생성_정보를_설정한다(Long source, Long target) {
            this.body = new FavoriteCreateRequest(source, target);
            return this;
        }

        public ExtractableResponse<Response> 즐겨찾기_생성_요청을_보낸다() {
            setAuthorization();
            return this.spec.contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(this.body)
                    .when().post(PREFIX_PATH)
                    .then().log().all()
                    .statusCode(statusCode)
                    .extract();
        }

        public ExtractableResponse<Response> 즐겨찾기_조회_요청을_보낸다() {
            setAuthorization();
            return this.spec.contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get(PREFIX_PATH)
                    .then().log().all()
                    .statusCode(statusCode)
                    .extract();
        }

        public ExtractableResponse<Response> 즐겨찾기_삭제_요청을_보낸다(String uri) {
            setAuthorization();
            return this.spec.contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete(uri)
                    .then().log().all()
                    .statusCode(statusCode)
                    .extract();
        }

        private void setAuthorization() {
            if (this.accessToken != null && !this.accessToken.isEmpty()) {
                this.spec.header(AUTHORIZATION, "Bearer " + this.accessToken);
            }
        }
    }
}
