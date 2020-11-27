package pl.project.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/endpoint")
public class EndpointController {
    @Autowired
    EndpointService endpointService;

    @GetMapping()
    @CrossOrigin(origins = "*")
    public List<Endpoint> getAllEndpoint() {
        return endpointService.getAllEndpoint();
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public Endpoint getEndpoint(@PathVariable Integer id) {
        return endpointService.getEndpoint(id);
    }

    @PostMapping()
    @CrossOrigin(origins = "*")
    public void addUpdateEndpoint(@RequestBody Endpoint endpoint) {
        endpointService.addUpdateEndpoint(endpoint);
    }

    @PutMapping()
    @CrossOrigin(origins = "*")
    public void updateTest(@RequestBody Endpoint endpoint) {
        endpointService.addUpdateEndpoint(endpoint);
    }

    @DeleteMapping(value = "/{id}")
    @CrossOrigin(origins = "*")
    public void deleteEndpoint(@PathVariable Integer id) {
        endpointService.deleteEndpoint(id);
    }
}
