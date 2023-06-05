package grafl.msg.lksgtoolirs.repositories;

import grafl.msg.lksgtoolirs.models.Bpn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BpnRepository extends JpaRepository<Bpn, String> {
}
