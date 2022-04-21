package org.theancients.placebackend.highlight;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

@Service
public class HighlightService {

    @Autowired
    private HighlightRepository highlightRepository;

    @Scheduled(fixedRate = 5000)
    private void removeOutdatedHighlights() {
        List<Highlight> allHighlights = highlightRepository.findAll();
        Set<Highlight> toDelete = new HashSet<>();
        Instant now = Instant.now();
        for (Highlight highlight : allHighlights) {
            long lastPing = Duration.between(highlight.getLastPing(), now).getSeconds();
            if (lastPing > 12) {
                toDelete.add(highlight);
            }
        }
        highlightRepository.deleteAll(toDelete);
    }

    public void updateHighlight(Highlight highlight) {
        // reject update if it contains sus data
        if (highlight == null || highlight.getId() == null) {
            return;
        }
        if (highlight.getId().length() < 16 || highlight.getId().length() > 32) {
            return;
        }
        if (!StringUtils.isAlphanumeric(highlight.getId())) {
            return;
        }
        if (highlight.getX() < 0 || highlight.getX() > 1023) {
            return;
        }
        if (highlight.getY() < 0 || highlight.getY() > 1023) {
            return;
        }
        //highlight.setInstant(Instant.now());
        highlightRepository.save(highlight);
    }

    public List<Point> updateHighlight(String userId, Point point) {
        // only update highlight if a valid point was selected
        if (point != null && point.x >= 0 && point.x < 128 && point.y >= 0 && point.y < 128) {
            Optional<Highlight> optionalHighlight = highlightRepository.findById(userId);
            Highlight highlight = optionalHighlight.orElse(new Highlight(userId));
            // update ping
            Instant now = Instant.now();
            highlight.setLastPing(now);
            // did point change?
            if (highlight.getX() != point.x || highlight.getY() != point.y) {
                highlight.setX(point.x);
                highlight.setY(point.y);
                highlight.setLastChange(now);
            }
            highlightRepository.save(highlight);
        }

        List<Highlight> allHighlights = highlightRepository.findAll();
        List<Point> activeHighlights = new ArrayList<>();
        for (Highlight highlight : allHighlights) {
            Instant now = Instant.now();
            long lastChange = Duration.between(highlight.getLastChange(), now).getSeconds();
            if (lastChange < 120 && !highlight.getId().equals(userId)) {
                activeHighlights.add(new Point(highlight.getX(), highlight.getY()));
            }
        }

        return activeHighlights;
    }

}
