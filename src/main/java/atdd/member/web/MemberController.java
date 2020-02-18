package atdd.member.web;

import atdd.member.application.dto.CreateMemberRequestView;
import atdd.member.application.dto.MemberResponseView;
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

    @PostMapping
    public ResponseEntity<MemberResponseView> join(@RequestBody CreateMemberRequestView view) {
        return ResponseEntity.created(URI.create("members/1"))
            .body(new MemberResponseView(1, "seok2@naver.com","이재석"));
    }

    @DeleteMapping("{email}")
    public ResponseEntity withdraw(@PathVariable String email){
        return ResponseEntity.noContent().build();
    }

}
