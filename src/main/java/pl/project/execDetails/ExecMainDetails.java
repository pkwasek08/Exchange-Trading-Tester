package pl.project.execDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.test.Test;
import pl.project.testParameters.TestParameter;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecMainDetails {
    private String testName;
    private Integer exeTime;
    private Integer dbTime;
    private Integer fullTestTime;
    private Integer userNumber;
    private Integer series;
    private Date date;
    private Integer numberOfRequests;
    private PriceDetails priceDetails;

    public static List<ExecMainDetails> map(List<Test> testList) {
        List<ExecMainDetails> execMainDetailsList = newLinkedList();
        testList.forEach(test -> {
            TestParameter testParameter = test.getTestParameter();
            execMainDetailsList.add(new ExecMainDetails(testParameter.getTestName(), test.getApiTestTime(), test.getDatabaseTestTime(), test.getApplicationTestTime(), testParameter.getNumberOfUsers(), testParameter.getSeries(),
                    test.getDate(), testParameter.getNumberOfRequests(),
                    new PriceDetails(testParameter.getMinBuyPrice(), testParameter.getMaxBuyPrice(), testParameter.getMinSellPrice(), testParameter.getMaxSellPrice(), testParameter.getVolumes())));
        });
        return execMainDetailsList;
    }

    public static ExecMainDetails map(Test test) {
        TestParameter testParameter = test.getTestParameter();
        return new ExecMainDetails(testParameter.getTestName(), test.getApiTestTime(), test.getDatabaseTestTime(), test.getApplicationTestTime(), testParameter.getNumberOfUsers(), testParameter.getSeries(),
                test.getDate(), testParameter.getNumberOfRequests(),
                new PriceDetails(testParameter.getMinBuyPrice(), testParameter.getMaxBuyPrice(), testParameter.getMinSellPrice(), testParameter.getMaxSellPrice(), testParameter.getVolumes()));
    }
}
