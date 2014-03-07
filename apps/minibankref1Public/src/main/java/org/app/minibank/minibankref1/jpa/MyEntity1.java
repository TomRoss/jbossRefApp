package org.app.minibank.minibankref1.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MyEntity1 {

    @Column
    @Id
    private String correlationId;

    @Column
    String name;

    @Column
    private String json;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " name=" + name + "; correlationId=" + correlationId;
    }

}
