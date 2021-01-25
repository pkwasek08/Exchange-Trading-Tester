package pl.project.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.project.execDetails.ExecDetails;
import pl.project.execDetails.ExecMainDetails;
import pl.project.execDetails.TestDetails;
import pl.project.testParameters.TestParameter;
import pl.project.testParameters.TestParameterService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    TestService testService;
    @Autowired
    TestParameterService testParameterService;


    @GetMapping()
    @CrossOrigin(origins = "*")
    public List<Test> getAllTest() {
        return testService.getAllTest();
    }

    @GetMapping("/AllDetails")
    @CrossOrigin(origins = "*")
    public List<ExecMainDetails> getAllTestDetails() {
        return ExecMainDetails.map(testService.getAllTest());
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public Test getTest(@PathVariable Integer id) {
        return testService.getTest(id);
    }

    @GetMapping("/simulate")
    @CrossOrigin(origins = "*")
    public List<ExecMainDetails> simulate(@RequestParam Integer numberUser, @RequestParam Integer numberSeries, @RequestParam Integer companyId, @RequestParam String companyName,
                                          @RequestParam Integer startUserMoney, @RequestParam Integer startStockNumber, @RequestParam String testName, @RequestParam Integer daysNumber) {
        List<ExecMainDetails> execMainDetailsList = newLinkedList();
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarStop = Calendar.getInstance();
        calendarStart.add(Calendar.DAY_OF_WEEK, -daysNumber + 1);
        while (!calendarStart.after(calendarStop)) {
            Date startTask = new Date();
            TestDetails testDetails = testService.simulate(numberUser, numberSeries, companyId, companyName, startUserMoney, startStockNumber, calendarStart.getTime());
            int fullTimeTask = (int) (new Date().getTime() - startTask.getTime());
            Test test = testService.addUpdateTest(new Test(0, testDetails.getExecDetails().getDbTime(), fullTimeTask, testDetails.getExecDetails().getExeTime(), null, null, calendarStart.getTime(),
                    testParameterService.addUpdateTestParameter(new TestParameter(0, numberUser, numberSeries, companyId, testDetails.getCompanyName(), startUserMoney, startStockNumber, testName,
                            testDetails.getNumberOfRequests(), testDetails.getPriceDetails().getMinBuyPrice(), testDetails.getPriceDetails().getMaxBuyPrice(),
                            testDetails.getPriceDetails().getMinSellPrice(), testDetails.getPriceDetails().getMaxSellPrice(), testDetails.getPriceDetails().getVolumes())), null));
            execMainDetailsList.add(ExecMainDetails.map(test));
            calendarStart.add(Calendar.DAY_OF_WEEK, 1);
        }
        return execMainDetailsList;
    }

    @PostMapping()
    @CrossOrigin(origins = "*")
    public void addTest(@RequestBody Test test) {
        testService.addUpdateTest(test);
    }

    @PostMapping("/signInUsers/{userNumber}")
    @CrossOrigin(origins = "*")
    public ExecDetails signUpUsers(@PathVariable Integer userNumber) {
        return testService.signOnUsers(userNumber);
    }

    @PutMapping()
    @CrossOrigin(origins = "*")
    public void updateTest(@RequestBody Test test) {
        testService.addUpdateTest(test);
    }

    @DeleteMapping(value = "/{id}")
    @CrossOrigin(origins = "*")
    public void deleteTest(@PathVariable Integer id) {
        testService.deleteTest(id);
    }
}
