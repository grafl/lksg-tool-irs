package grafl.msg.lksgtoolirs.models;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@AllArgsConstructor
@Builder
@Data
@Entity
@Jacksonized
@NoArgsConstructor
@Table(name = "investigation_bpns", schema = "lksg_tool_irs")
@IdClass(InvestigationBpnPK.class)
public class InvestigationBpn implements Serializable {

    @Id
    @Column(name = "investigation")
    private String investigation;
    @Id
    @Column(name = "bpn")
    private String bpn;

}
