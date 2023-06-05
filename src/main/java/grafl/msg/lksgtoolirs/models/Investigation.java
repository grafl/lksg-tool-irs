package grafl.msg.lksgtoolirs.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
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
@Table(name = "investigations", schema = "lksg_tool_irs")
public class Investigation implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "callback_url")
    private String callbackUrl;

    @Column(name = "bom_lifecycle")
    private String bomLifecycle;

    @Column(name = "global_asset_id")
    private String globalAssetId;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "investigation_bpns", schema = "lksg_tool_irs",
            joinColumns = @JoinColumn(name = "investigation", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "bpn", referencedColumnName = "bpn"))
    List<Bpn> incidentBpns;

}
