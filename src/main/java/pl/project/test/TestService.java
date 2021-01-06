package pl.project.test;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.project.components.OfferLimitDTO;
import pl.project.components.StockSellBuy;
import pl.project.components.User;
import pl.project.components.UserStockDTO;
import pl.project.execDetails.ExecDetails;
import pl.project.execDetails.ExecDetailsOfferLimit;
import pl.project.execDetails.ExecDetailsUser;
import pl.project.execDetails.ExecDetailsUserStock;

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

    public ExecDetailsUser getUserById(Integer userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        ResponseEntity<ExecDetailsUser> response = this.restTemplate.getForEntity("http://localhost:8080/user/details/" + userId, ExecDetailsUser.class);
        return response.getBody();
    }

    public ExecDetails simulate(Integer numberUser, Integer numberSeries) {
        ExecDetails execDetails = new ExecDetails(0, 0);
        ExecDetailsUser execDetailsUser = signOnUsers(createNewUserList(numberUser));
        List<User> userList = execDetailsUser.getUserList();
        updateExecDetails(execDetails, new ExecDetails(execDetailsUser.getExeTime(), execDetailsUser.getDbTime()));
        updateExecDetails(execDetails, buyStocks(userList, 10));
        for (int i = 0; i < numberSeries; i++) {
            updateExecDetails(execDetails, addBuyLimitOffer(userList));
            updateExecDetails(execDetails, addSellLimitOffer(userList));
            updateExecDetails(execDetails, sellStocks(createRandomUserList(userList)));
            updateExecDetails(execDetails, buyStocks(createRandomUserList(userList), null));
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
        try {
            for (User user : userList) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                ExecDetailsUser execDetailsUser = getUserById(user.getId());
                updateExecDetails(execDetails, new ExecDetails(execDetailsUser.getExeTime(), execDetailsUser.getDbTime()));
                ExecDetailsOfferLimit execDetailsOfferLimit = getFirstOfferLimit(1, "Sell", user.getId());
                updateExecDetails(execDetails, execDetailsOfferLimit.getExecDetails());
                int amount = isNull(stockNumber) ? random.nextInt(10) + 1 : stockNumber;
                if (execDetailsUser.getUser().getCash() <= 0 || execDetailsOfferLimit.getOfferLimitDTO().getAmount() == 0
                        || (int) (execDetailsUser.getUser().getCash() / execDetailsOfferLimit.getOfferLimitDTO().getPrice()) <= 0) {
                    amount = 0;
                } else if (execDetailsUser.getUser().getCash() < amount * execDetailsOfferLimit.getOfferLimitDTO().getPrice()) {
                    amount = (random.nextInt((int) (execDetailsUser.getUser().getCash() / execDetailsOfferLimit.getOfferLimitDTO().getPrice()))) + 1;
                }
                if (amount > 0) {
                    StockSellBuy stockBuy = new StockSellBuy(amount, "Buy", new Date(), 1, user.getId());
                    HttpEntity<Object> requestEntity = new HttpEntity<Object>(stockBuy, headers);
                    this.restTemplate.postForEntity("http://localhost:8080/offerSellBuy/newOffer", requestEntity, ExecDetails.class);
                }
            }
        } catch (Exception e) {
            log.error("BuyStocks: " + e.getLocalizedMessage());
        }
        return execDetails;
    }

    public ExecDetails addBuyLimitOffer(List<User> userList) {
        ExecDetails execDetails = new ExecDetails(0, 0);
        try {
            for (User user : userList) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                ExecDetailsOfferLimit execDetailsSellOfferLimit = getFirstOfferLimit(1, "Sell", user.getId());
                updateExecDetails(execDetails, execDetailsSellOfferLimit.getExecDetails());
                int amount = 0;
                float price = 0;
                if (isNull(execDetailsSellOfferLimit.getOfferLimitDTO().getPrice()) || execDetailsSellOfferLimit.getOfferLimitDTO().getPrice() == 0) {
                    ExecDetailsOfferLimit execDetailsBuyOfferLimit = getFirstOfferLimit(1, "Buy", user.getId());
                    updateExecDetails(execDetails, execDetailsBuyOfferLimit.getExecDetails());
                    price = execDetailsBuyOfferLimit.getOfferLimitDTO().getPrice() * (1 - ((float) (random.nextInt(10) + 1) / 100));
                } else {
                    price = execDetailsSellOfferLimit.getOfferLimitDTO().getPrice() * (1 - ((float) random.nextInt(10) + 1) / 100);
                }
                ExecDetailsUser execDetailsUser = getUserById(user.getId());
                updateExecDetails(execDetails, new ExecDetails(execDetailsUser.getExeTime(), execDetailsUser.getDbTime()));
                price = (float) (Math.round(price * 100.0) / 100.0);
                amount = random.nextInt(10) + 1;
                if (execDetailsUser.getUser().getCash() <= 0 || (int) (execDetailsUser.getUser().getCash() / price) <= 0) {
                    amount = 0;
                } else if (execDetailsUser.getUser().getCash() < amount * price) {
                    amount = (random.nextInt((int) (execDetailsUser.getUser().getCash() / price))) + 1;
                }
                if (amount > 0) {
                    OfferLimitDTO stockBuy = new OfferLimitDTO(amount, "Buy", new Date(), 1, user.getId(), price);
                    HttpEntity<Object> requestEntity = new HttpEntity<Object>(stockBuy, headers);
                    this.restTemplate.postForEntity("http://localhost:8080/offerSellBuyLimit/newOfferLimit", requestEntity, ExecDetails.class);
                }
            }
        } catch (Exception e) {
            log.error("AddBuyLimitOffer: " + e.getLocalizedMessage());
        }
        return execDetails;
    }

    public ExecDetails addSellLimitOffer(List<User> userList) {
        ExecDetails execDetails = new ExecDetails(0, 0);
        try {
            for (User user : userList) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                ExecDetailsUserStock execDetailsUserStock = getStockByUserIdAndCompanyId(user.getId(), 1);
                updateExecDetails(execDetails, execDetailsUserStock.getExecDetails());
                int amount;
                float price;
                if (isNull(execDetailsUserStock.getUserStockDTO().getActualPrice()) || execDetailsUserStock.getUserStockDTO().getActualPrice() == 0) {
                    ExecDetailsOfferLimit execDetailsOfferLimit = getFirstOfferLimit(1, "Buy", user.getId());
                    updateExecDetails(execDetails, execDetailsOfferLimit.getExecDetails());
                    price = execDetailsOfferLimit.getOfferLimitDTO().getPrice() * (1 + ((float) (random.nextInt(10) + 1) / 100));
                } else {
                    price = execDetailsUserStock.getUserStockDTO().getActualPrice() * (1 + ((float) (random.nextInt(10) + 1) / 100));
                }
                price = (float) (Math.round(price * 100.0) / 100.0);
                if (execDetailsUserStock.getUserStockDTO().getAmount() == 0) {
                    amount = 0;
                } else if (execDetailsUserStock.getUserStockDTO().getAmount() >= 10) {
                    amount = random.nextInt(10) + 1;
                } else {
                    amount = random.nextInt(execDetailsUserStock.getUserStockDTO().getAmount()) + 1;
                }
                if (amount > 0) {
                    OfferLimitDTO stockBuy = new pl.project.components.OfferLimitDTO(amount, "Sell", new Date(), 1, user.getId(), price);
                    HttpEntity<Object> requestEntity = new HttpEntity<Object>(stockBuy, headers);
                    this.restTemplate.postForEntity("http://localhost:8080/offerSellBuyLimit/newOfferLimit", requestEntity, ExecDetails.class);
                }
            }
        } catch (Exception e) {
            log.error("AddSellLimitOffer: " + e.getLocalizedMessage());
        }
        return execDetails;
    }

    public ExecDetailsOfferLimit getFirstOfferLimit(Integer companyId, String type, Integer userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (type.equals("Buy")) {
            ResponseEntity<ExecDetailsOfferLimit> response = this.restTemplate.getForEntity("http://localhost:8080/offerSellBuyLimit/buy/company/first/" + companyId + "/user/" + userId
                    , ExecDetailsOfferLimit.class);
            return new ExecDetailsOfferLimit(response.getBody().getExecDetails(), response.getBody().getOfferLimitDTO());
        } else {
            ResponseEntity<ExecDetailsOfferLimit> response = this.restTemplate.getForEntity("http://localhost:8080/offerSellBuyLimit/sell/company/first/" + companyId + "/user/" + userId
                    , ExecDetailsOfferLimit.class);
            return new ExecDetailsOfferLimit(response.getBody().getExecDetails(), response.getBody().getOfferLimitDTO());
        }
    }

    public ExecDetailsUserStock getStockByUserIdAndCompanyId(Integer userId, Integer companyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        ResponseEntity<ExecDetailsUserStock> response = this.restTemplate.getForEntity("http://localhost:8080/stock/company/" + companyId + "/user/" + userId, ExecDetailsUserStock.class);
        return new ExecDetailsUserStock(response.getBody().getExecDetails(), response.getBody().getUserStockDTO());
    }

    public ExecDetails sellStocks(List<User> userList) {
        ExecDetails execDetails = new ExecDetails(0, 0);
        try {
            for (User user : userList) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                ExecDetailsUserStock execDetailsUserStock = getStockByUserIdAndCompanyId(user.getId(), 1);
                updateExecDetails(execDetails, execDetailsUserStock.getExecDetails());
                UserStockDTO userStockDTO = execDetailsUserStock.getUserStockDTO();
                ExecDetailsOfferLimit execDetailsOfferLimit = getFirstOfferLimit(1, "Buy", user.getId());
                updateExecDetails(execDetails, execDetailsOfferLimit.getExecDetails());
                int amount = 0;
                if (userStockDTO.getAmount() > 0) {
                    amount = random.nextInt(10) + 1;
                }
                if (amount > userStockDTO.getAmount()) {
                    amount = random.nextInt(userStockDTO.getAmount()) + 1;
                }
                if (amount > execDetailsOfferLimit.getOfferLimitDTO().getAmount()) {
                    amount = execDetailsOfferLimit.getOfferLimitDTO().getAmount();
                }
                if (amount > 0) {
                    StockSellBuy stockSell = new StockSellBuy(amount, "Sell", new Date(), 1, user.getId());
                    HttpEntity<Object> requestEntity = new HttpEntity<Object>(stockSell, headers);
                    this.restTemplate.postForEntity("http://localhost:8080/offerSellBuy", requestEntity, ExecDetails.class);
                }
            }
        } catch (Exception e) {
            log.error("SellStocks: " + e.getLocalizedMessage());
        }
        return execDetails;
    }
}
