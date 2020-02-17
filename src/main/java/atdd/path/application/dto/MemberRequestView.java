package atdd.path.application.dto;

public class MemberRequestView
{
    private String email;
    private String name;
    private String password;

    public MemberRequestView() {}

    public MemberRequestView(String email, String name, String password)
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

}
