package nextstep.member.domain;

import nextstep.member.domain.stub.GithubResponses;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
public class GithubMemberRepository {
    private Map<String, GithubMember> members = new HashMap<>();

    @PostConstruct
    public void setUp() {
        List<GithubMember> githubMembers =
                Arrays.stream(GithubResponses.values())
                .map(r -> new GithubMember(r.getCode(), r.getAccessToken(), r.getEmail()))
                .collect(toList());
        for (GithubMember gm : githubMembers) {
            members.put(gm.getCode(), gm);
        }
    }

    public Optional<GithubMember> findByCode(String code) {
        return Optional.ofNullable(members.get(code));
    }

    public GithubMember save(String code, String accessToken, String email) {
        GithubMember githubMember = new GithubMember(code, accessToken, email);
        members.put(githubMember.getCode(), githubMember);
        return githubMember;
    }

}
