package atdd.path.web;

import atdd.path.application.dto.CreateMemberRequestView;
import atdd.path.application.dto.MemberResponseView;
import atdd.path.dao.MemberDao;
import atdd.path.domain.Member;
import atdd.path.exception.MemberNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
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
			Member persistMember = memberDao.findById(id).orElseThrow(MemberNotFoundException::new);
			return ResponseEntity.ok(MemberResponseView.of(persistMember));
		} catch (EmptyResultDataAccessException e) {
			throw new MemberNotFoundException(e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteMember(@PathVariable Long id) {
		memberDao.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
