package grafl.msg.lksgtoolirs.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@AllArgsConstructor
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class InvestigationBpnPK implements Serializable {

    @Id
    @Column(name = "investigation")
    private String investigation;
    @Id
    @Column(name = "bpn")
    private String bpn;

}
