package pl.project.testParameters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestParameterService {
    Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    TestParameterRepository testParameterRepository;

    public List<TestParameter> getAllTestParameter() {
        List<TestParameter> testParameterList = new ArrayList<>();
        testParameterRepository.findAll().forEach(testParameterList::add);
        return testParameterList;
    }

    public TestParameter getTestParameter(Integer id) {
        return testParameterRepository.findById(id).get();
    }

    public TestParameter addUpdateTestParameter(TestParameter testParameter) {
        return testParameterRepository.save(testParameter);
    }


    public void deleteTestParameter(Integer id) {
        testParameterRepository.deleteById(id);
    }
}