package atdd.path.application.dto;

import atdd.path.domain.Member;

public class CreateMemberRequestView
{
    private String email;
    private String name;
    private String password;

    public CreateMemberRequestView(String email, String name, String password)
    {
        this.email = email;
        this.name = name;
        this.password = password;
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

    public Member toMember()
    {
        return new Member(email, name, password);
    }
}
