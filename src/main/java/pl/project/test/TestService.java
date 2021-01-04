package pl.project.test;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.project.components.StockSellBuy;
import pl.project.components.StockSellBuyLimit;
import pl.project.components.User;
import pl.project.execDetails.ExecDetails;
import pl.project.execDetails.ExecDetailsUser;

import java.sql.Timestamp;
import java.util.*;

import static java.util.Objects.isNull;

@Service
public class TestService {
    Logger log = LogManager.getLogger(this.getClass());
    Faker faker = new Faker();
    @Autowired
    TestRepository testRepository;

    private final RestTemplate restTemplate;
    Random random = new Random();

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

    public ExecDetails signOnUsers(Integer numberUsers) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        List<User> userList = createNewUserList(numberUsers);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(userList, headers);
        ResponseEntity<ExecDetails> response = this.restTemplate.postForEntity("http://localhost:8080/user/addUserList", requestEntity, ExecDetails.class);
        return response.getBody();
    }

    public List<User> createNewUserList(Integer numberUsers) {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < numberUsers; i++) {
            userList.add(new User(faker.name().firstName(), faker.name().lastName(), "email" + i + "@test.com", String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()), 10000f));
        }
        return userList;
    }

    public ExecDetailsUser signOnUsers(List<User> userList) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(userList, headers);
        ResponseEntity<ExecDetailsUser> response = this.restTemplate.postForEntity("http://localhost:8080/user/addUserList", requestEntity, ExecDetailsUser.class);
        return response.getBody();
    }

    public ExecDetails simulate(Integer numberUser, Integer numberSeries) {
        ExecDetails execDetails = new ExecDetails(0, 0);
        ExecDetailsUser execDetailsUser = signOnUsers(createNewUserList(numberUser));
        List<User> userList = execDetailsUser.getUserList();
        log.info("1111111 " + execDetails);
        updateExecDetails(execDetails, new ExecDetails(execDetails.getExeTime(), execDetails.getDbTime()));
        log.info("22222222222 " + execDetails);
        updateExecDetails(execDetails, buyStocks(createRandomUserList(userList), 10));
        log.info("333333 " + execDetails);
        for (int i = 0; i < numberSeries; i++) {
            updateExecDetails(execDetails, buyStocks(createRandomUserList(userList), null));
            log.info("444444444 " + execDetails);
            updateExecDetails(execDetails, sellStocks(createRandomUserList(userList)));
            log.info("55555555 " + execDetails);
        }
        return execDetails;
    }

    public List<User> createRandomUserList(List<User> userList) {
        for (int i = 0; i < userList.size() / 2; i++) {
            int randomIndex = random.nextInt(userList.size());
            userList.remove(randomIndex);
        }
        return userList;
    }

    public ExecDetails updateExecDetails(ExecDetails mainExecDetails, ExecDetails newExecDetails) {
        mainExecDetails.setDbTime(mainExecDetails.getDbTime() + newExecDetails.getDbTime());
        mainExecDetails.setExeTime(mainExecDetails.getExeTime() + newExecDetails.getExeTime());
        return mainExecDetails;
    }

    public ExecDetails buyStocks(List<User> userList, Integer stockNumber) {
        ExecDetails execDetails = new ExecDetails(0, 0);
        for (User user : userList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            int amount = isNull(stockNumber) ? random.nextInt(10) + 1 : stockNumber;
            StockSellBuy stockBuy = new StockSellBuy(amount, "Buy", new Date(), 1, user.getId());
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(stockBuy, headers);
            ResponseEntity<ExecDetails> response = this.restTemplate.postForEntity("http://localhost:8080/offerSellBuy/newOffer", requestEntity, ExecDetails.class);
            ExecDetails execDetailsBuyStock = new ExecDetails(response.getBody().getExeTime(), response.getBody().getDbTime());
            execDetails.setDbTime(execDetails.getDbTime() + execDetailsBuyStock.getDbTime());
            execDetails.setExeTime(execDetails.getExeTime() + execDetailsBuyStock.getExeTime());
        }
        return execDetails;
    }

    public ExecDetails addBuyLimitOffer(List<User> userList, Integer stockNumber) {
        ExecDetails execDetails = new ExecDetails(0, 0);
        for (User user : userList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            int amount = isNull(stockNumber) ? random.nextInt(10) + 1 : stockNumber;
            // rozwiązać problem z ceną limitową,
            StockSellBuyLimit stockBuy = new StockSellBuyLimit(amount, "Buy", new Date(), 1, user.getId(), 110f);
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(stockBuy, headers);
            ResponseEntity<ExecDetails> response = this.restTemplate.postForEntity("http://localhost:8080/offerSellBuyLimit/newOfferLimit", requestEntity, ExecDetails.class);
            ExecDetails execDetailsBuyStock = new ExecDetails(response.getBody().getExeTime(), response.getBody().getDbTime());
            execDetails.setDbTime(execDetails.getDbTime() + execDetailsBuyStock.getDbTime());
            execDetails.setExeTime(execDetails.getExeTime() + execDetailsBuyStock.getExeTime());
        }
        return execDetails;
    }

    public Integer getFirstOfferPrice(Integer companyId, String type) {
        return 0;
    }

    public ExecDetails sellStocks(List<User> userList) {
        ExecDetails execDetails = new ExecDetails(0, 0);
        for (User user : userList) {
            ExecDetails execDetailsBuyStock;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            int amount = random.nextInt(10) + 1;
            StockSellBuy stockSell = new StockSellBuy(amount, "Sell", new Date(), 1, user.getId());
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(stockSell, headers);
            ResponseEntity<ExecDetails> response = this.restTemplate.postForEntity("http://localhost:8080/offerSellBuy", requestEntity, ExecDetails.class);
            execDetailsBuyStock = response.getBody();
            execDetails.setDbTime(execDetails.getDbTime() + execDetailsBuyStock.getDbTime());
            execDetails.setExeTime(execDetails.getExeTime() + execDetailsBuyStock.getExeTime());
        }
        return execDetails;
    }
}
