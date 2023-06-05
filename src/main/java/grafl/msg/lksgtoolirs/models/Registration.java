package grafl.msg.lksgtoolirs.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Registration {

    private String id;
    private String company;
    private String bpn;

}
