package civia.service;

import civia.exception.DuplicateGrievanceException;
import civia.model.Defect;
import civia.repository.GrievanceRepository;
import java.util.List;

public class GrievanceDispatcher {
    private final GrievanceRepository repository;
    private final TriageCalculable triageEngine;

    public GrievanceDispatcher(GrievanceRepository repository, TriageCalculable triageEngine) {
        this.repository = repository;
        this.triageEngine = triageEngine;
    }

    public synchronized void logGrievance(Defect newDefect) throws DuplicateGrievanceException {
        for (Defect existing : repository.findAll()) {
            if (HaversineDeduplicationService.isSpatialDuplicate(existing, newDefect)) {
                throw new DuplicateGrievanceException("Duplicate defect detected near " + existing.getLocationName());
            }
        }
        int score = triageEngine.computeTriageScore(newDefect);
        newDefect.setPriorityScore(score);
        newDefect.advanceStatus();
        repository.save(newDefect);
    }

    public List<Defect> getPrioritizedQueue() {
        return repository.findAll().stream()
                .sorted((a, b) -> Integer.compare(b.getPriorityScore(), a.getPriorityScore()))
                .toList();
    }
}
