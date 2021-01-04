package pl.project.execDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecMainDetails {
    private ExecDetails execDetails;
    private Integer fullTime;
}
