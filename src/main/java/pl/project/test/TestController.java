package pl.project.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.project.execDetails.ExecDetails;
import pl.project.execDetails.ExecMainDetails;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    TestService testService;

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
        ExecDetails execDetails = testService.simulate(numberUser, numberSeries);
        return new ExecMainDetails(execDetails, (int) (new Date().getTime() - startTask.getTime()));
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
