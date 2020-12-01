package pl.project.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.project.execDetails.ExecDetails;

import java.util.List;

@RestController
@RequestMapping(value = "/test")
public class TestController {
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

    @PostMapping()
    @CrossOrigin(origins = "*")
    public void addTest(@RequestBody Test test) {
        testService.addUpdateTest(test);
    }

    @PostMapping("/signInUsers/{userNumber}")
    @CrossOrigin(origins = "*")
    public ExecDetails signInUsers(@PathVariable Integer userNumber) {
        return testService.signInUser(userNumber);
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
