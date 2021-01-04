package pl.project.components;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockSellBuy {
    private Integer amount;
    private String type;
    private Date date;
    private Integer CompanyId;
    private Integer userId;
}
