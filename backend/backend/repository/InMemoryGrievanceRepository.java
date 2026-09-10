package civia.repository;

import civia.model.Defect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InMemoryGrievanceRepository implements GrievanceRepository {
    private final List<Defect> storage = new ArrayList<>();

    @Override
    public void save(Defect defect) { storage.add(defect); }

    @Override
    public List<Defect> findAll() { return Collections.unmodifiableList(storage); }

    @Override
    public Optional<Defect> findById(String ticketId) {
        return storage.stream().filter(d -> d.getTicketId().equalsIgnoreCase(ticketId)).findFirst();
    }

    @Override
    public int count() { return storage.size(); }
}
