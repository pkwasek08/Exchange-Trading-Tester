package pl.project.execDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.components.UserStockDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecDetailsUserStock {
    private ExecDetails execDetails;
    private UserStockDTO userStockDTO;
}
