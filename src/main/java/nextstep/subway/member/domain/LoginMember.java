package nextstep.subway.member.domain;


import nextstep.subway.auth.domain.UserDetails;

public class LoginMember extends UserDetails {
    public LoginMember() {
        super();
    }

    public LoginMember(Long id, String email, String password, Integer age) {
        super(id, email, password, age);
    }

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }
}
