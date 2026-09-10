package civia.service;

import civia.model.Defect;

public interface TriageCalculable {
    int computeTriageScore(Defect defect);
}
