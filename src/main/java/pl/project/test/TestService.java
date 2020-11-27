package pl.project.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {
    Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    TestRepository testRepository;

    public List<Test> getAllTest() {
        List<Test> testList = new ArrayList<>();
        testRepository.findAll().forEach(testList::add);
        return testList;
    }

    public Test getTest(Integer id) {
        return testRepository.findById(id).get();
    }

    public void addUpdateTest(Test test) {
        testRepository.save(test);
    }

    public void deleteTest(Integer id) {
        testRepository.deleteById(id);
    }
}
