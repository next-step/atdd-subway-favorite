package atdd.member.application;

import atdd.member.dao.MemberDao;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void authenticate(String email, String password) {
        Optional.ofNullable(memberDao.findByEmail(email))
            .filter(m -> m.isMatchPassword(password))
            .orElseThrow(IllegalArgumentException::new);
    }
}
