package grafl.msg.lksgtoolirs.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "bpns", schema = "lksg_tool_irs")
public class Bpn implements Serializable {

    @Id
    @Column(name = "bpn")
    private String bpn;

    @ManyToMany(mappedBy = "incidentBpns")
    List<Investigation> investigations;

}
