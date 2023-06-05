package grafl.msg.lksgtoolirs.repositories;

import grafl.msg.lksgtoolirs.models.Investigation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestigationRepository extends JpaRepository<Investigation, String> {
}
