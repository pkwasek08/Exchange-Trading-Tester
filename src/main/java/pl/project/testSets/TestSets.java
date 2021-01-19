package pl.project.testSets;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test_sets")
public class TestSets {
    private int id;
    private Integer numberOfUsers;
    private Integer series;
    private Integer companyId;
    private String companyName;
    private Integer startUserMoney;
    private Integer startStockNumber;
    private Integer days;
    private String testName;

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
    @Column(name = "company_id", nullable = true)
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Basic
    @Column(name = "company_name", nullable = true)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Basic
    @Column(name = "start_user_money", nullable = true)
    public Integer getStartUserMoney() {
        return startUserMoney;
    }

    public void setStartUserMoney(Integer startUserMoney) {
        this.startUserMoney = startUserMoney;
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
    @Column(name = "test_name", nullable = true, length = -1)
    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    @Basic
    @Column(name = "days", nullable = true)
    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestSets testSets = (TestSets) o;
        return id == testSets.id &&
                Objects.equals(numberOfUsers, testSets.numberOfUsers) &&
                Objects.equals(series, testSets.series) &&
                Objects.equals(companyId, testSets.companyId) &&
                Objects.equals(startUserMoney, testSets.startUserMoney) &&
                Objects.equals(startStockNumber, testSets.startStockNumber) &&
                Objects.equals(testName, testSets.testName) &&
                Objects.equals(companyName, testSets.companyName) &&
                Objects.equals(days, testSets.days);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfUsers, series, companyId, companyName, startUserMoney, startStockNumber, days, testName);
    }

    @Override
    public String toString() {
        return "TestSets{" +
                "id=" + id +
                ", numberOfUsers=" + numberOfUsers +
                ", series=" + series +
                ", companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", startUserMoney=" + startUserMoney +
                ", startStockNumber=" + startStockNumber +
                ", days='" + days + '\'' +
                ", testName='" + testName + '\'' +
                '}';
    }
}
