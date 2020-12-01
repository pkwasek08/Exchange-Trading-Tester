package pl.project.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.project.components.User;
import pl.project.execDetails.ExecDetails;

import java.util.*;

@Service
public class TestService {
    Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    TestRepository testRepository;

    private final RestTemplate restTemplate;

    public TestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

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

    public ExecDetails signInUser(Integer numberUsers) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < numberUsers; i++) {
            userList.add(new User("user" + i, "lastname" + i, "email" + i + "@test.com", "password" + i));
        }
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(userList, headers);
        ResponseEntity<ExecDetails> response = this.restTemplate.postForEntity("http://localhost:8080/user/addUserList", requestEntity, ExecDetails.class);
        return response.getBody();
    }
}
