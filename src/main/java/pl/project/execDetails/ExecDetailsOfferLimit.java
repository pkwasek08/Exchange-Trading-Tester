package pl.project.execDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.components.OfferLimitDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecDetailsOfferLimit {
    private ExecDetails execDetails;
    private OfferLimitDTO offerLimitDTO;
}
