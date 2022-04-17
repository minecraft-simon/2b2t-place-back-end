package org.theancients.placebackend.highlight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/highlights")
public class HighlightController {

    @Autowired
    private HighlightService highlightService;

    @GetMapping
    public String getHighlights() {
        Highlight highlight = new Highlight("asdf", 1, 2, Instant.now(), Instant.now());
        highlightService.updateHighlight(highlight);
        return "asdf";
    }

    @PutMapping
    public String updateHighlight(@RequestBody Highlight highlight) {
        highlightService.updateHighlight(highlight);
        return "{}";
    }


}
