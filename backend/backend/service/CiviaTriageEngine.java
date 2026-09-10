package civia.service;

import civia.model.Defect;
import civia.model.PotholeDefect;
import civia.model.StreetlightDefect;

public class CiviaTriageEngine implements TriageCalculable {
    @Override
    public int computeTriageScore(Defect defect) {
        int score = 25;
        if (defect instanceof PotholeDefect p) {
            score += (int) (p.getDepthInches() * 12);
            if (p.isHighSpeedCorridor()) score += 30;
        } else if (defect instanceof StreetlightDefect s) {
            score += s.isBlackoutBlock() ? 50 : 20;
        }
        return Math.min(score, 100);
    }
}
