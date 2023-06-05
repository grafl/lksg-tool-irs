package grafl.msg.lksgtoolirs.repositories;

import grafl.msg.lksgtoolirs.models.Analysis;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, String>  {

    public Optional<Analysis> findByInvestigation(String investigation);

}
