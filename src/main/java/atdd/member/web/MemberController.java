package atdd.member.web;

import atdd.member.application.dto.CreateMemberRequestView;
import atdd.member.application.dto.MemberResponseView;
import atdd.member.dao.MemberDao;
import atdd.member.domain.Member;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("members")
public class MemberController {
    private static final String DEFAULT_URL = "members";
    private final MemberDao memberDao;

    public MemberController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @PostMapping
    public ResponseEntity<MemberResponseView> join(@RequestBody CreateMemberRequestView view) {
        Member member = memberDao.save(view.toMember());
        return ResponseEntity.created(URI.create(DEFAULT_URL + "/" + member.getId()))
            .body(MemberResponseView.of(member));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> withdraw(@PathVariable long id){
        return ResponseEntity.noContent().build();
    }

}
