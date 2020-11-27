package pl.project.test;

import pl.project.endpoints.Endpoint;
import pl.project.testParameters.TestParameter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tests")
public class Test {
    private int id;
    private Integer userId;
    private Integer databaseTestTime;
    private Integer applicationTestTime;
    private Integer apiTestTime;
    private BigDecimal avgCpuUsed;
    private BigDecimal avgRamUsed;
    private TestParameter testParameter;
    private Endpoint endpoint;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id", nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "database_test_time", nullable = true)
    public Integer getDatabaseTestTime() {
        return databaseTestTime;
    }

    public void setDatabaseTestTime(Integer databaseTestTime) {
        this.databaseTestTime = databaseTestTime;
    }

    @Basic
    @Column(name = "application_test_time", nullable = true)
    public Integer getApplicationTestTime() {
        return applicationTestTime;
    }

    public void setApplicationTestTime(Integer applicationTestTime) {
        this.applicationTestTime = applicationTestTime;
    }

    @Basic
    @Column(name = "api_test_time", nullable = true)
    public Integer getApiTestTime() {
        return apiTestTime;
    }

    public void setApiTestTime(Integer apiTestTime) {
        this.apiTestTime = apiTestTime;
    }

    @Basic
    @Column(name = "avg_cpu_used", nullable = true, precision = 2)
    public BigDecimal getAvgCpuUsed() {
        return avgCpuUsed;
    }

    public void setAvgCpuUsed(BigDecimal avgCpuUsed) {
        this.avgCpuUsed = avgCpuUsed;
    }

    @Basic
    @Column(name = "avg_ram_used", nullable = true, precision = 2)
    public BigDecimal getAvgRamUsed() {
        return avgRamUsed;
    }

    public void setAvgRamUsed(BigDecimal avgRamUsed) {
        this.avgRamUsed = avgRamUsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Test test = (Test) o;

        if (id != test.id) return false;
        if (userId != null ? !userId.equals(test.userId) : test.userId != null) return false;
        if (databaseTestTime != null ? !databaseTestTime.equals(test.databaseTestTime) : test.databaseTestTime != null)
            return false;
        if (applicationTestTime != null ? !applicationTestTime.equals(test.applicationTestTime) : test.applicationTestTime != null)
            return false;
        if (apiTestTime != null ? !apiTestTime.equals(test.apiTestTime) : test.apiTestTime != null) return false;
        if (avgCpuUsed != null ? !avgCpuUsed.equals(test.avgCpuUsed) : test.avgCpuUsed != null) return false;
        if (avgRamUsed != null ? !avgRamUsed.equals(test.avgRamUsed) : test.avgRamUsed != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (databaseTestTime != null ? databaseTestTime.hashCode() : 0);
        result = 31 * result + (applicationTestTime != null ? applicationTestTime.hashCode() : 0);
        result = 31 * result + (apiTestTime != null ? apiTestTime.hashCode() : 0);
        result = 31 * result + (avgCpuUsed != null ? avgCpuUsed.hashCode() : 0);
        result = 31 * result + (avgRamUsed != null ? avgRamUsed.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "test_parameters_id", referencedColumnName = "id")
    public TestParameter getTestParameter() {
        return testParameter;
    }

    public void setTestParameter(TestParameter testParameter) {
        this.testParameter = testParameter;
    }

    @ManyToOne
    @JoinColumn(name = "endpoint_id", referencedColumnName = "id")
    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }
}
