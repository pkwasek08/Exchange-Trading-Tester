package pl.project.testSets;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestSetsRepository extends CrudRepository<TestSets, Integer> {
    @Query("select NEW pl.project.testSets.TestSetsInfoDTO(ts.id, ts.testName) from TestSets ts")
    List<TestSetsInfoDTO> getAllTestSetsInfo();
}
