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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public Test getTest(@PathVariable Integer id) {
        return testService.getTest(id);
    }

    @GetMapping("/simulate")
    @CrossOrigin(origins = "*")
    public ExecMainDetails simulate(@RequestParam Integer numberUser, @RequestParam Integer numberSeries) {
        Date startTask = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TestDetails testDetails = testService.simulate(numberUser, numberSeries);
        int fullTimeTask = (int) (new Date().getTime() - startTask.getTime());
        testService.addUpdateTest(new Test(0, testDetails.getExecDetails().getDbTime(), fullTimeTask, testDetails.getExecDetails().getExeTime(), null, null,
                testParameterService.addUpdateTestParameter(new TestParameter(0, numberUser, "simulate " + dt.format(startTask),
                        testDetails.getPriceDetails().getNumberOfRequests(),  testDetails.getPriceDetails().getMinBuyPrice(),  testDetails.getPriceDetails().getMaxBuyPrice(),
                        testDetails.getPriceDetails().getMinSellPrice(),  testDetails.getPriceDetails().getMaxSellPrice(),  testDetails.getPriceDetails().getVolumes())), null));
        return new ExecMainDetails(testDetails.getExecDetails(), fullTimeTask);
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
