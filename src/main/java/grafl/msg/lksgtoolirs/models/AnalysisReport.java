package grafl.msg.lksgtoolirs.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

@AllArgsConstructor
@Builder
@Data
@Entity
@Immutable
@NoArgsConstructor
@Table(name = "v_analysis_report", schema = "lksg_tool_irs")
public class AnalysisReport {

    @Id
    private String investigation;
    private String global_asset_id;
    private String bpn;
    private String state;
    private String button_state;

}
