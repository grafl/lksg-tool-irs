package grafl.msg.lksgtoolirs.models;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "analyses", schema = "lksg_tool_irs")
public class Analysis implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "investigation")
    String investigation;

    @Column(name = "state")
    String state;

    @Column(name = "impacted")
    String impacted;

    @Column(name = "aspect")
    String aspect;

}
