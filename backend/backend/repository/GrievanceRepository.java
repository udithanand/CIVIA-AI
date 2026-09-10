package civia.repository;

import civia.model.Defect;
import java.util.List;
import java.util.Optional;

public interface GrievanceRepository {
    void save(Defect defect);
    List<Defect> findAll();
    Optional<Defect> findById(String ticketId);
    int count();
}
