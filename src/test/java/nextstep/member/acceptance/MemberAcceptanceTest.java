package nextstep.member.acceptance;

import io.restassured.RestAssured;
import nextstep.member.dto.MemberRequest;
import nextstep.support.AcceptanceTest;
import nextstep.support.AssertUtils;
import nextstep.support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.UNDEFINED_PORT;
import static nextstep.auth.acceptance.step.AuthStep.일반_로그인_요청;
import static nextstep.member.acceptance.step.MemberStep.*;
import static nextstep.member.fixture.MemberFixture.회원_정보_DTO;
import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
class MemberAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup databaseCleanUp;

    @BeforeEach
    void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
        }
        databaseCleanUp.execute();
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        var response = 회원_생성_요청(회원_정보_DTO);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        var 회원_생성_응답 = 회원_생성_요청(회원_정보_DTO);

        // when
        var 회원_조회_응답 = 회원_정보_조회_요청(회원_생성_응답);

        // then
        String 이메일 = 회원_정보_DTO.getEmail();
        Integer 나이 = 회원_정보_DTO.getAge();
        회원_정보_조회됨(회원_조회_응답, 이메일, 나이);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        var 회원_생성_응답 = 회원_생성_요청(회원_정보_DTO);

        // when
        MemberRequest 수정할_회원_정보 = MemberRequest.builder()
                .email("updatedTest@gmail.com")
                .password("updatedtestpassword123")
                .age(27)
                .build();
        var 회원_수정_응답 = 회원_정보_수정_요청(회원_생성_응답, 수정할_회원_정보);

        // then
        AssertUtils.assertThatStatusCode(회원_수정_응답, HttpStatus.OK);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        var 회원_생성_응답 = 회원_생성_요청(회원_정보_DTO);

        // when
        var 회원_삭제_응답 = 회원_삭제_요청(회원_생성_응답);

        // then
        AssertUtils.assertThatStatusCode(회원_삭제_응답, HttpStatus.NO_CONTENT);
    }

    /**
     * Given 회원 가입을 생성하고
     * Given 로그인을 하고
     * When 토큰을 통해 내 정보를 조회하면
     * Then 내 정보를 조회할 수 있다
     */
    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        회원_생성_요청(회원_정보_DTO);

        var 로그인_응답 = 일반_로그인_요청(회원_정보_DTO.getEmail(), 회원_정보_DTO.getPassword());

        // when
        var 내_정보_조회_응답 = 내_정보_조회_요청(로그인_응답);

        // then
        String 이메일 = 회원_정보_DTO.getEmail();
        Integer 나이 = 회원_정보_DTO.getAge();
        회원_정보_조회됨(내_정보_조회_응답, 이메일, 나이);
    }
}