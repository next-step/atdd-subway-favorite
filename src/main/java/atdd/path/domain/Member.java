package atdd.path.domain;

public class Member
{
    private Long id;
    private String email;
    private String name;
    private String password;

    public Member() {}

    public Member(Long id, String email, String name, String password)
    {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Member(String email, String name, String password)
    {
        this.email = email;
        this.name = name;
        this.password = password;
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
