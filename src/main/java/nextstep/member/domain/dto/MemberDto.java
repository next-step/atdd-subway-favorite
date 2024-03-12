package nextstep.member.domain.dto;

import nextstep.auth.application.dto.UserDetailDto;
import nextstep.member.domain.Member;

public class MemberDto implements UserDetailDto {
    private Long id;
    private String email;
    private Integer age;
    private String password;

    public MemberDto() {
    }

    public MemberDto(Long id, String email, Integer age, String password) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    public static MemberDto of(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getAge(), member.getPassword());
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

    public String getPassword() {
        return password;
    }
}
