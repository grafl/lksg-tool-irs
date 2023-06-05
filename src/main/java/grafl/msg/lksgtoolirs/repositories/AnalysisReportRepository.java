package grafl.msg.lksgtoolirs.repositories;

import grafl.msg.lksgtoolirs.models.AnalysisReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisReportRepository extends JpaRepository<AnalysisReport, String>  {
}
