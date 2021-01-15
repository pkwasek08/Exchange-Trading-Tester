package pl.project.execDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.components.CompanyInfoDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecDetailsCompany {
    ExecDetails execDetails;
    List<CompanyInfoDTO> companyIdList;
}
