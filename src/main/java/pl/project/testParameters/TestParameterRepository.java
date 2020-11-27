package pl.project.testParameters;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestParameterRepository extends CrudRepository<TestParameter, Integer> {
}
