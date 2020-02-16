package atdd.path.web;

import atdd.path.application.dto.CreateMemberRequestView;
import atdd.path.application.dto.MemberResponseView;
import atdd.path.application.exception.NoDataException;
import atdd.path.dao.MemberDao;
import atdd.path.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {

	private MemberDao memberDao;

	public MemberController(final MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	@PostMapping
	public ResponseEntity createMember(@RequestBody CreateMemberRequestView view) {
		Member persistMember = memberDao.save(view.toMember());
		return ResponseEntity
			.created(URI.create("/members/" + persistMember.getId()))
			.body(MemberResponseView.of(persistMember));
	}

	@GetMapping("/{id}")
	public ResponseEntity retrieveMember(@PathVariable Long id) {
		try {
			Member persistMember = memberDao.findById(id).orElseThrow(NoDataException::new);
			return ResponseEntity.ok(MemberResponseView.of(persistMember));
		} catch (NoDataException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
