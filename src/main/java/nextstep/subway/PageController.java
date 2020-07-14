package nextstep.subway;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "index";
    }

    @GetMapping(value = "/stations", produces = MediaType.TEXT_HTML_VALUE)
    public String stations() {
        return "index";
    }

    @GetMapping(value = "/lines", produces = MediaType.TEXT_HTML_VALUE)
    public String lines() {
        return "index";
    }

    @GetMapping(value = "/edges", produces = MediaType.TEXT_HTML_VALUE)
    public String lineStations() { return "index"; }

    @GetMapping(value = "/maps", produces = MediaType.TEXT_HTML_VALUE)
    public String maps() {
        return "index";
    }

    @GetMapping(value = "/path", produces = MediaType.TEXT_HTML_VALUE)
    public String path() {
        return "index";
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String login() {
        return "index";
    }

    @GetMapping(value = "/join", produces = MediaType.TEXT_HTML_VALUE)
    public String join() {
        return "index";
    }

    @GetMapping(value = "/mypage", produces = MediaType.TEXT_HTML_VALUE)
    public String mypage() {
        return "index";
    }

    @GetMapping(value = "/mypage/edit", produces = MediaType.TEXT_HTML_VALUE)
    public String mypageEdit() {
        return "index";
    }

    @GetMapping(value = "/favorites", produces = MediaType.TEXT_HTML_VALUE)
    public String favorites() {
        return "index";
    }
}
