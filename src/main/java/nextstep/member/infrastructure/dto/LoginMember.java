package nextstep.member.infrastructure.dto;

import nextstep.member.domain.Member;

public class LoginMember {

    private Long id;

    private String email;

    private int age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, int age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember from(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}
