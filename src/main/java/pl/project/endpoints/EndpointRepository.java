package pl.project.endpoints;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndpointRepository extends CrudRepository<Endpoint, Integer> {
}