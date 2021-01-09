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
import pl.project.execDetails.*;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
            userList.add(new User(faker.name().firstName(), faker.name().lastName(), "email" + i + "@test.com", String.valueOf((new Date()).getTime() + faker.number().randomDigit()), 100000f));
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

    public TestDetails simulate(Integer numberUser, Integer numberSeries) {
        TestDetails testDetails = new TestDetails(new ExecDetails(0, 0), new PriceDetails(0, 100000f, 0f, 100000f, 0f, 0));
        ExecDetailsUser execDetailsUser = signOnUsers(createNewUserList(numberUser));
        List<User> userList = execDetailsUser.getUserList();
        updateExecDetails(testDetails.getExecDetails(), new ExecDetails(execDetailsUser.getExeTime(), execDetailsUser.getDbTime()));
        updateTransactionExecDetails(testDetails, buyStocks(userList, 10));
        for (int i = 0; i < numberSeries; i++) {
            updateExecDetails(testDetails.getExecDetails(), addSellLimitOffer(userList));
            updateExecDetails(testDetails.getExecDetails(), addBuyLimitOffer(userList));
            updateTransactionExecDetails(testDetails, sellStocks(userList));
            updateTransactionExecDetails(testDetails, buyStocks(userList, null));
        }
        return testDetails;
    }

    public List<User> createRandomUserList(List<User> userList) {
        List<User> randomUserList = new LinkedList<>();
        randomUserList.addAll(userList);
        for (int i = 0; i < userList.size() / 2; i++) {
            int randomIndex = random.nextInt(randomUserList.size());
            randomUserList.remove(randomIndex);
        }
        return randomUserList;
    }

    public ExecDetails updateExecDetails(ExecDetails mainExecDetails, ExecDetails newExecDetails) {
        mainExecDetails.setDbTime(mainExecDetails.getDbTime() + newExecDetails.getDbTime());
        mainExecDetails.setExeTime(mainExecDetails.getExeTime() + newExecDetails.getExeTime());
        return mainExecDetails;
    }

    public TestDetails updateTransactionExecDetails(TestDetails mainExecDetails, TransactionDetails transactionDetails) {
        if (nonNull(transactionDetails.getPrice()) && transactionDetails.getPrice() > 0) {
            if (transactionDetails.getType().equals("Sell")) {
                if (mainExecDetails.getPriceDetails().getMaxSellPrice() < transactionDetails.getPrice()) {
                    mainExecDetails.getPriceDetails().setMaxSellPrice(transactionDetails.getPrice());
                }
                if (mainExecDetails.getPriceDetails().getMinSellPrice() > transactionDetails.getPrice()) {
                    mainExecDetails.getPriceDetails().setMinSellPrice(transactionDetails.getPrice());
                }
            } else {
                if (mainExecDetails.getPriceDetails().getMaxBuyPrice() < transactionDetails.getPrice()) {
                    mainExecDetails.getPriceDetails().setMaxBuyPrice(transactionDetails.getPrice());
                }
                if (mainExecDetails.getPriceDetails().getMinBuyPrice() > transactionDetails.getPrice()) {
                    mainExecDetails.getPriceDetails().setMinBuyPrice(transactionDetails.getPrice());
                }
            }
        }
        mainExecDetails.getPriceDetails().setVolumes(mainExecDetails.getPriceDetails().getVolumes() + transactionDetails.getAmount());
        mainExecDetails.getPriceDetails().setNumberOfRequests(mainExecDetails.getPriceDetails().getNumberOfRequests() + transactionDetails.getNumberRequest());

        mainExecDetails.getExecDetails().setDbTime(mainExecDetails.getExecDetails().getDbTime() + transactionDetails.getExecDetails().getDbTime());
        mainExecDetails.getExecDetails().setExeTime(mainExecDetails.getExecDetails().getExeTime() + transactionDetails.getExecDetails().getExeTime());
        return mainExecDetails;
    }

    public TransactionDetails buyStocks(List<User> userList, Integer stockNumber) {
        TransactionDetails transactionDetails = new TransactionDetails(0, 0f, 0, "Buy", new ExecDetails(0, 0));
        try {
            for (User user : userList) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                ExecDetailsUser execDetailsUser = getUserById(user.getId());
                updateExecDetails(transactionDetails.getExecDetails(), new ExecDetails(execDetailsUser.getExeTime(), execDetailsUser.getDbTime()));
                ExecDetailsOfferLimit execDetailsOfferLimit = getOfferLimitList(1, "Sell", user.getId());
                updateExecDetails(transactionDetails.getExecDetails(), execDetailsOfferLimit.getExecDetails());
                if (execDetailsOfferLimit.getOfferLimitDTOList().isEmpty()) {
                    break;
                }
                int amountStockFromOfferLimit = getAmountStockFromOfferLimit(execDetailsOfferLimit.getOfferLimitDTOList());
                int amount = isNull(stockNumber) ? random.nextInt(10) + 1 : stockNumber;
                float price = 0;
                if (execDetailsUser.getUser().getCash() <= 0 || amountStockFromOfferLimit == 0) {
                    amount = 0;
                } else {
                    price = getPriceStockFromOfferLimitByAmount(execDetailsOfferLimit.getOfferLimitDTOList(), amount, user.getCash());
                    if (price > user.getCash()) {
                        amount -= getAvailableAmountStockFromOfferLimit(execDetailsOfferLimit.getOfferLimitDTOList(), amount, user.getCash());
                    }
                }
                if (amount > 0) {
                    transactionDetails.setAmount(amount);
                    transactionDetails.setPrice(getOfferSettledByAmount(execDetailsOfferLimit.getOfferLimitDTOList(), amount).getPrice());
                    StockSellBuy stockBuy = new StockSellBuy(amount, "Buy", new Date(), 1, user.getId());
                    HttpEntity<Object> requestEntity = new HttpEntity<Object>(stockBuy, headers);
                    this.restTemplate.postForEntity("http://localhost:8080/offerSellBuy/newOffer", requestEntity, ExecDetails.class);
                }
            }
        } catch (Exception e) {
            log.error("BuyStocks: " + e.getCause());
        }
        return transactionDetails;
    }

    public TransactionDetails sellStocks(List<User> userList) {
        TransactionDetails transactionDetails = new TransactionDetails(0, 0f, 0, "Sell", new ExecDetails(0, 0));
        try {
            for (User user : userList) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                ExecDetailsUserStock execDetailsUserStock = getStockByUserIdAndCompanyId(user.getId(), 1);
                updateExecDetails(transactionDetails.getExecDetails(), execDetailsUserStock.getExecDetails());
                UserStockDTO userStockDTO = execDetailsUserStock.getUserStockDTO();
                ExecDetailsOfferLimit execDetailsOfferLimit = getOfferLimitList(1, "Buy", user.getId());
                updateExecDetails(transactionDetails.getExecDetails(), execDetailsOfferLimit.getExecDetails());
                if (execDetailsOfferLimit.getOfferLimitDTOList().isEmpty()) {
                    break;
                }
                int amount = 0;
                if (userStockDTO.getAmount() > 0) {
                    amount = random.nextInt(10) + 1;
                }
                if (amount > userStockDTO.getAmount()) {
                    amount = userStockDTO.getAmount();
                }
                int amountStockOfferLimit = getAmountStockFromOfferLimit(execDetailsOfferLimit.getOfferLimitDTOList());
                if (amount > amountStockOfferLimit) {
                    amount = amountStockOfferLimit;
                }
                if (amount > 0) {
                    transactionDetails.setAmount(amount);
                    transactionDetails.setPrice(getOfferSettledByAmount(execDetailsOfferLimit.getOfferLimitDTOList(), amount).getPrice());
                    StockSellBuy stockSell = new StockSellBuy(amount, "Sell", new Date(), 1, user.getId());
                    HttpEntity<Object> requestEntity = new HttpEntity<Object>(stockSell, headers);
                    this.restTemplate.postForEntity("http://localhost:8080/offerSellBuy", requestEntity, ExecDetails.class);
                }
            }
        } catch (Exception e) {
            log.error("SellStocks: " + e.getLocalizedMessage());
        }
        return transactionDetails;
    }

    private OfferLimitDTO getOfferSettledByAmount(List<OfferLimitDTO> offerLimitDTOList, int amount) {
        for (OfferLimitDTO offerLimitDTO : offerLimitDTOList) {
            if (offerLimitDTO.getAmount() >= amount) {
                return offerLimitDTO;
            } else {
                amount -= offerLimitDTO.getAmount();
            }
        }
        return new OfferLimitDTO();
    }

    private int getAvailableAmountStockFromOfferLimit(List<OfferLimitDTO> offerLimitDTOList, int amount, float cash) {
        float price = 0;
        int amountStock = 0;
        for (OfferLimitDTO offerLimitDTO : offerLimitDTOList) {
            float tempPrice = 0;
            if (offerLimitDTO.getAmount() > amountStock) {
                tempPrice = offerLimitDTO.getPrice() * amountStock;
                amountStock = 0;
            } else {
                tempPrice = offerLimitDTO.getPrice() * offerLimitDTO.getAmount();
                amountStock -= offerLimitDTO.getAmount();
            }
            if (price + tempPrice > cash) {
                for (int i = 1; i <= offerLimitDTO.getAmount() && i <= amount; i++) {
                    if (price + i * offerLimitDTO.getPrice() > cash) {
                        return amountStock - (i - 1);
                    }
                }
                return amountStock;
            }
            if (amountStock <= 0) {
                return amountStock;
            } else {
                amount -= amountStock;
            }
            price += tempPrice;
        }
        return amount;
    }

    private float getPriceStockFromOfferLimitByAmount(List<OfferLimitDTO> offerLimitDTOList, int amount, float cash) {
        float price = 0f;
        int amountStock = 0;
        for (OfferLimitDTO offerLimitDTO : offerLimitDTOList) {
            float tempPrice = 0f;
            if (offerLimitDTO.getAmount() > amountStock) {
                tempPrice = offerLimitDTO.getPrice() * amountStock;
                amountStock = 0;
            } else {
                tempPrice = offerLimitDTO.getPrice() * offerLimitDTO.getAmount();
                amountStock -= offerLimitDTO.getAmount();
            }
            if (price + tempPrice <= cash) {
                for (int i = 1; i <= offerLimitDTO.getAmount() && i <= amount; i++) {
                    if (price + i * offerLimitDTO.getPrice() > cash) {
                        return price + (i - 1) * offerLimitDTO.getPrice();
                    }
                }
                return price + amount * offerLimitDTO.getPrice();
            }
            if (amountStock <= 0) {
                return price;
            } else {
                amount -= amountStock;
            }
            price += tempPrice;
        }
        return price;
    }

    private int getAmountStockFromOfferLimit(List<OfferLimitDTO> offerLimitDTOList) {
        return offerLimitDTOList.stream().mapToInt(OfferLimitDTO::getAmount).sum();
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
                    price = execDetailsBuyOfferLimit.getOfferLimitDTO().getPrice() * (1 - ((float) (random.nextInt(5) + 1) / 100));
                } else {
                    price = execDetailsSellOfferLimit.getOfferLimitDTO().getPrice() * (1 - ((float) random.nextInt(5) + 1) / 100);
                }
                ExecDetailsUser execDetailsUser = getUserById(user.getId());
                updateExecDetails(execDetails, new ExecDetails(execDetailsUser.getExeTime(), execDetailsUser.getDbTime()));
                price = (float) (Math.round(price * 100.0) / 100.0);
                amount = random.nextInt(5) + 1;
                if (execDetailsUser.getUser().getCash() <= 0 || (int) (execDetailsUser.getUser().getCash() / price) <= 0) {
                    amount = 0;
                } else if (execDetailsUser.getUser().getCash() < amount * price) {
                    amount = (random.nextInt((int) (execDetailsUser.getUser().getCash() / price))) + 1;
                }
                if (amount > 0 && price > 0) {
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
                    price = execDetailsOfferLimit.getOfferLimitDTO().getPrice() * (1 + ((float) (random.nextInt(5) + 1) / 100));
                } else {
                    price = execDetailsUserStock.getUserStockDTO().getActualPrice() * (1 + ((float) (random.nextInt(5) + 1) / 100));
                }
                price = (float) (Math.round(price * 100.0) / 100.0);
                if (execDetailsUserStock.getUserStockDTO().getAmount() == 0) {
                    amount = 0;
                } else if (execDetailsUserStock.getUserStockDTO().getAmount() >= 10) {
                    amount = random.nextInt(5) + 1;
                } else {
                    amount = random.nextInt(execDetailsUserStock.getUserStockDTO().getAmount()) + 1;
                }
                if (amount > 0 && price > 0) {
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

    public ExecDetailsOfferLimit getOfferLimitList(Integer companyId, String type, Integer userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (type.equals("Buy")) {
            ResponseEntity<ExecDetailsOfferLimit> response = this.restTemplate.getForEntity("http://localhost:8080/offerSellBuyLimit/buy/company/details/" + companyId + "/user/" + userId
                    , ExecDetailsOfferLimit.class);
            return new ExecDetailsOfferLimit(response.getBody().getExecDetails(), response.getBody().getOfferLimitDTOList());
        } else {
            ResponseEntity<ExecDetailsOfferLimit> response = this.restTemplate.getForEntity("http://localhost:8080/offerSellBuyLimit/sell/company/details/" + companyId + "/user/" + userId
                    , ExecDetailsOfferLimit.class);
            return new ExecDetailsOfferLimit(response.getBody().getExecDetails(), response.getBody().getOfferLimitDTOList());
        }
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

}
