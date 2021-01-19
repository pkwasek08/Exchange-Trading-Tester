package pl.project.testSets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestSetsService {
    Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    TestSetsRepository testSetsRepository;

    public List<TestSets> getAllTestSets() {
        List<TestSets> testSetsList = new ArrayList<>();
        testSetsRepository.findAll().forEach(testSetsList::add);
        return testSetsList;
    }

    public TestSets getTestSets(Integer id) {
        return testSetsRepository.findById(id).get();
    }

    public List<TestSetsInfoDTO> getTestSetsInfoList(){
        return testSetsRepository.getAllTestSetsInfo();
    }

    public void addUpdateTestSets(TestSets testSets) {
        testSetsRepository.save(testSets);
    }

    public void deleteTestSets(Integer id) {
        testSetsRepository.deleteById(id);
    }
}
