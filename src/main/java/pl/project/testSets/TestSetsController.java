package pl.project.testSets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/testSets")
public class TestSetsController {
    @Autowired
    TestSetsService testSetsService;

    @GetMapping()
    @CrossOrigin(origins = "*")
    public List<TestSets> getAllTestSets() {
        return testSetsService.getAllTestSets();
    }

    @GetMapping("/info")
    @CrossOrigin(origins = "*")
    public List<TestSetsInfoDTO> getAllTestSetsInfo() {
        return testSetsService.getTestSetsInfoList();
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public TestSets getTestSets(@PathVariable Integer id) {
        return testSetsService.getTestSets(id);
    }

    @PostMapping()
    @CrossOrigin(origins = "*")
    public void addUpdateTestSets(@RequestBody TestSets testSets) {
        testSetsService.addUpdateTestSets(testSets);
    }

    @PutMapping()
    @CrossOrigin(origins = "*")
    public void updateTestSets(@RequestBody TestSets testSets) {
        testSetsService.addUpdateTestSets(testSets);
    }

    @DeleteMapping(value = "/{id}")
    @CrossOrigin(origins = "*")
    public void deleteTestSets(@PathVariable Integer id) {
        testSetsService.deleteTestSets(id);
    }
}
