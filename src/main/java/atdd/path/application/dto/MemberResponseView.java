package atdd.path.application.dto;

import atdd.path.domain.Member;

public class MemberResponseView
{
    private Long id;
    private String email;
    private String name;
    private String password;

    public MemberResponseView(Long id, String email, String name, String password)
    {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password =password;
    }

    public static MemberResponseView of(Member member)
    {
        return new MemberResponseView(member.getId(), member.getEmail(), member.getName(), member.getPassword());
    }

    public Long getId()
    {
        return id;
    }

    public String getEmail()
    {
        return email;
    }

    public String getName()
    {
        return name;
    }

    public String getPassword()
    {
        return password;
    }
}
