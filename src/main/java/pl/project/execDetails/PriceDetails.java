package pl.project.execDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDetails {
    private Integer numberOfRequests;
    private Float minBuyPrice;
    private Float maxBuyPrice;
    private Float minSellPrice;
    private Float maxSellPrice;
    private Integer volumes;
}
