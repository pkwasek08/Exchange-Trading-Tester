package pl.project.endpoints;

import javax.persistence.*;

@Entity
@Table(name = "endpoints")
public class Endpoint {
    private int id;
    private String endpoint;
    private String httpMethod;
    private String parameters;

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
    @Column(name = "endpoint", nullable = true, length = -1)
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Basic
    @Column(name = "http_method", nullable = true, length = -1)
    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Basic
    @Column(name = "parameters", nullable = true, length = -1)
    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Endpoint endpoint1 = (Endpoint) o;

        if (id != endpoint1.id) return false;
        if (endpoint != null ? !endpoint.equals(endpoint1.endpoint) : endpoint1.endpoint != null) return false;
        if (httpMethod != null ? !httpMethod.equals(endpoint1.httpMethod) : endpoint1.httpMethod != null) return false;
        if (parameters != null ? !parameters.equals(endpoint1.parameters) : endpoint1.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (endpoint != null ? endpoint.hashCode() : 0);
        result = 31 * result + (httpMethod != null ? httpMethod.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
