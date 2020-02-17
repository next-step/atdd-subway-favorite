package atdd.path.web;

import atdd.path.application.dto.CreateMemberRequestView;
import atdd.path.application.dto.MemberResponseView;
import atdd.path.dao.MemberDao;
import atdd.path.domain.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController
{
    public static final String MEMBER_URL = "/members";
    private MemberDao memberDao;

    public MemberController(MemberDao memberDao)
    {
        this.memberDao = memberDao;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody CreateMemberRequestView view)
    {
        Member persistMember = memberDao.save(view.toMember());
        return ResponseEntity
                .created(URI.create(MEMBER_URL + "/" +persistMember.getId()))
                .body(MemberResponseView.of(persistMember));
    }

    @GetMapping("/{id}")
    public ResponseEntity retrieveMember(@PathVariable Long id)
    {
        try
        {
            Member persistMember = memberDao.findById(id);
            return ResponseEntity.ok().body(MemberResponseView.of(persistMember));
        }
        catch (EmptyResultDataAccessException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMember(@PathVariable Long id)
    {
        memberDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
