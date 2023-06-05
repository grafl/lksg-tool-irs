package grafl.msg.lksgtoolirs.repositories;

import grafl.msg.lksgtoolirs.models.InvestigationBpn;
import grafl.msg.lksgtoolirs.models.InvestigationBpnPK;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestigationBpnRepository extends JpaRepository<InvestigationBpn, InvestigationBpnPK> {

    public Optional<List<InvestigationBpn>> findByInvestigation(String investigation);

}
