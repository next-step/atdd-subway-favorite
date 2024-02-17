package nextstep.utils.resthelper;

import static io.restassured.RestAssured.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import io.restassured.specification.RequestSpecification;
import nextstep.api.member.application.dto.MemberRequest;

/**
 * @author : Rene Choi
 * @since : 2024/02/13
 */

public class MemberRequestExecutor extends AbstractRequestExecutor {

	private static final String MEMBER_URL_PATH = "/members";

	private static RequestSpecification getRequestSpecification() {
		return given().log().all().contentType(APPLICATION_JSON_VALUE);
	}

	public static void createMember(MemberRequest memberRequest) {
		getRequestSpecification()
			.body(memberRequest)
			.when().post(MEMBER_URL_PATH)
			.then().log().all()
			.statusCode(CREATED.value()).extract();
	}

}
