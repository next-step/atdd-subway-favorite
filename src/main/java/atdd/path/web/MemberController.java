package atdd.path.web;

import atdd.path.application.dto.CreateMemberRequestView;
import atdd.path.application.dto.MemberResponseView;
import atdd.path.dao.MemberDao;
import atdd.path.domain.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RequestMapping("/members")
@RestController
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    private final MemberDao memberDao;

    public MemberController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @PostMapping
    public ResponseEntity<MemberResponseView> createMember(@RequestBody CreateMemberRequestView view,
                                                           HttpServletRequest request) {

        final Member savedMember = memberDao.save(view.toMember());
        return ResponseEntity.created(URI.create(request.getServletPath() +"/"+ savedMember.getId()))
                .body(new MemberResponseView(savedMember));
    }

}
