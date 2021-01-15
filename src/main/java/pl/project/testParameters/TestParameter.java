package pl.project.testParameters;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test_parameters")
public class TestParameter {
    private int id;
    private Integer numberOfUsers;
    private Integer series;
    private Integer companyId;
    private String companyName;
    private Integer startUserMoney;
    private Integer startStockNumber;
    private String testName;
    private Integer numberOfRequests;
    private Float minBuyPrice;
    private Float maxBuyPrice;
    private Float minSellPrice;
    private Float maxSellPrice;
    private Integer volumes;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "number_of_users", nullable = true)
    public Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    @Basic
    @Column(name = "series", nullable = true)
    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    @Basic
    @Column(name = "test_name", nullable = true, length = -1)
    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    @Basic
    @Column(name = "number_of_requests", nullable = true)
    public Integer getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(Integer numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }

    @Basic
    @Column(name = "min_buy_price", nullable = true, precision = 4)
    public Float getMinBuyPrice() {
        return minBuyPrice;
    }

    public void setMinBuyPrice(Float minBuyPrice) {
        this.minBuyPrice = minBuyPrice;
    }

    @Basic
    @Column(name = "max_buy_price", nullable = true, precision = 4)
    public Float getMaxBuyPrice() {
        return maxBuyPrice;
    }

    public void setMaxBuyPrice(Float maxBuyPrice) {
        this.maxBuyPrice = maxBuyPrice;
    }

    @Basic
    @Column(name = "min_sell_price", nullable = true, precision = 4)
    public Float getMinSellPrice() {
        return minSellPrice;
    }

    public void setMinSellPrice(Float minSellPrice) {
        this.minSellPrice = minSellPrice;
    }

    @Basic
    @Column(name = "max_sell_price", nullable = true, precision = 4)
    public Float getMaxSellPrice() {
        return maxSellPrice;
    }

    public void setMaxSellPrice(Float maxSellPrice) {
        this.maxSellPrice = maxSellPrice;
    }

    @Basic
    @Column(name = "volumes", nullable = true)
    public Integer getVolumes() {
        return volumes;
    }

    public void setVolumes(Integer volumes) {
        this.volumes = volumes;
    }

    @Basic
    @Column(name = "company_id", nullable = true)
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Basic
    @Column(name = "start_user_money", nullable = true)
    public Integer getStartUserMoney() {
        return startUserMoney;
    }

    public void setStartUserMoney(Integer startUSerMoney) {
        this.startUserMoney = startUSerMoney;
    }

    @Basic
    @Column(name = "start_stock_number", nullable = true)
    public Integer getStartStockNumber() {
        return startStockNumber;
    }

    public void setStartStockNumber(Integer startStockNumber) {
        this.startStockNumber = startStockNumber;
    }

    @Basic
    @Column(name = "company_name", nullable = true)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestParameter that = (TestParameter) o;

        if (id != that.id) return false;
        if (numberOfUsers != null ? !numberOfUsers.equals(that.numberOfUsers) : that.numberOfUsers != null)
            return false;
        if (testName != null ? !testName.equals(that.testName) : that.testName != null) return false;
        if (numberOfRequests != null ? !numberOfRequests.equals(that.numberOfRequests) : that.numberOfRequests != null)
            return false;
        if (minBuyPrice != null ? !minBuyPrice.equals(that.minBuyPrice) : that.minBuyPrice != null) return false;
        if (maxBuyPrice != null ? !maxBuyPrice.equals(that.maxBuyPrice) : that.maxBuyPrice != null) return false;
        if (minSellPrice != null ? !minSellPrice.equals(that.minSellPrice) : that.minSellPrice != null) return false;
        if (maxSellPrice != null ? !maxSellPrice.equals(that.maxSellPrice) : that.maxSellPrice != null) return false;
        if (volumes != null ? !volumes.equals(that.volumes) : that.volumes != null) return false;
        if (series != null ? !series.equals(that.series) : that.series != null) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (startUserMoney != null ? !startUserMoney.equals(that.startUserMoney) : that.startUserMoney != null) return false;
        if (startStockNumber != null ? !startStockNumber.equals(that.startStockNumber) : that.startStockNumber != null) return false;
        if (companyName != null ? !companyName.equals(that.companyName) : that.companyName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (numberOfUsers != null ? numberOfUsers.hashCode() : 0);
        result = 31 * result + (testName != null ? testName.hashCode() : 0);
        result = 31 * result + (numberOfRequests != null ? numberOfRequests.hashCode() : 0);
        result = 31 * result + (minBuyPrice != null ? minBuyPrice.hashCode() : 0);
        result = 31 * result + (maxBuyPrice != null ? maxBuyPrice.hashCode() : 0);
        result = 31 * result + (minSellPrice != null ? minSellPrice.hashCode() : 0);
        result = 31 * result + (maxSellPrice != null ? maxSellPrice.hashCode() : 0);
        result = 31 * result + (volumes != null ? volumes.hashCode() : 0);
        result = 31 * result + (series != null ? series.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (startUserMoney != null ? startUserMoney.hashCode() : 0);
        result = 31 * result + (startStockNumber != null ? startStockNumber.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        return result;
    }
}
