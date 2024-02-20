package subway.favorite;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

	@PostMapping
	public ResponseEntity<Void> save() {
		return ResponseEntity.created(URI.create("")).body(null);
	}
}
