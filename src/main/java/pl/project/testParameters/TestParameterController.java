package pl.project.testParameters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/testParameter")
public class TestParameterController {
    @Autowired
    TestParameterService testParameterService;

    @GetMapping()
    @CrossOrigin(origins = "*")
    public List<TestParameter> getAllTestParameter() {
        return testParameterService.getAllTestParameter();
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public TestParameter getTestParameter(@PathVariable Integer id) {
        return testParameterService.getTestParameter(id);
    }

    @PostMapping()
    @CrossOrigin(origins = "*")
    public void addTestParameter(@RequestBody TestParameter testParameter) {
        testParameterService.addUpdateTestParameter(testParameter);
    }

    @PutMapping()
    @CrossOrigin(origins = "*")
    public void updateTestParameter(@RequestBody TestParameter testParameter) {
        testParameterService.addUpdateTestParameter(testParameter);
    }

    @DeleteMapping(value = "/{id}")
    @CrossOrigin(origins = "*")
    public void deleteTestParameter(@PathVariable Integer id) {
        testParameterService.deleteTestParameter(id);
    }
}
