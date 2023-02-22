package nextstep.member.application.dto;

import nextstep.member.domain.Member;

import java.util.List;

public class MemberResponse {

    private Long id;
    private String email;
    private Integer age;
    private List<String> roles;

    public MemberResponse(Long id, String email, Integer age, List<String> roles) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.roles = roles;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getAge(), member.getRoles());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public List<String> getRoles() {
        return this.roles;
    }

}
