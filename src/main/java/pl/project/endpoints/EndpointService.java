package pl.project.endpoints;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EndpointService {
    Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    EndpointRepository endpointRepository;

    public List<Endpoint> getAllEndpoint() {
        List<Endpoint> endpointList = new ArrayList<>();
        endpointRepository.findAll().forEach(endpointList::add);
        return endpointList;
    }

    public Endpoint getEndpoint(Integer id) {
        return endpointRepository.findById(id).get();
    }

    public void addUpdateEndpoint(Endpoint endpoint) {
        endpointRepository.save(endpoint);
    }

    public void deleteEndpoint(Integer id) {
        endpointRepository.deleteById(id);
    }

}