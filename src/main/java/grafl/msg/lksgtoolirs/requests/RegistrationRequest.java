package grafl.msg.lksgtoolirs.requests;

import grafl.msg.lksgtoolirs.models.Bpn;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RegistrationRequest {

    private String bomLifecycle;
    private String callbackUrl;
    private String globalAssetId;
    private List<String> incidentBpns;

    public List<Bpn> bpns() {
        List<Bpn> rv = new ArrayList<>();
        for(String bpn : incidentBpns) {
            rv.add(Bpn.builder().bpn(bpn).build());
        }
        return rv;
    }

}
