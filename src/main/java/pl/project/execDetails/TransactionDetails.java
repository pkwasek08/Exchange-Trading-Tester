package pl.project.execDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetails {
    Integer amount;
    Float price;
    Integer numberRequest;
    String type;
    ExecDetails execDetails;
}
