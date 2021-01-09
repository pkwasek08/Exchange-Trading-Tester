package pl.project.execDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.components.OfferLimitDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecDetailsOfferLimit {
    private ExecDetails execDetails;
    private OfferLimitDTO offerLimitDTO;
    private List<OfferLimitDTO> offerLimitDTOList;

    public ExecDetailsOfferLimit(ExecDetails execDetails, OfferLimitDTO offerLimitDTO) {
        this.execDetails = execDetails;
        this.offerLimitDTO = offerLimitDTO;
    }

    public ExecDetailsOfferLimit(ExecDetails execDetails, List<OfferLimitDTO> offerLimitDTOList) {
        this.execDetails = execDetails;
        this.offerLimitDTOList = offerLimitDTOList;
    }
}
