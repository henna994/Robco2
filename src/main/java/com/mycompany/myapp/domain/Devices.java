package com.mycompany.myapp.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Devices.
 */
@Entity
@Table(name = "devices")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "devices")
public class Devices implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "model")
    private Integer model;

    @Column(name = "registered")
    private String registered;

    @Column(name = "availability")
    private String availability;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "department")
    private String department;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Devices name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getModel() {
        return model;
    }

    public Devices model(Integer model) {
        this.model = model;
        return this;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public String getRegistered() {
        return registered;
    }

    public Devices registered(String registered) {
        this.registered = registered;
        return this;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getAvailability() {
        return availability;
    }

    public Devices availability(String availability) {
        this.availability = availability;
        return this;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getType() {
        return type;
    }

    public Devices type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public Devices department(String department) {
        this.department = department;
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Devices devices = (Devices) o;
        if (devices.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), devices.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Devices{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", model=" + getModel() +
            ", registered='" + getRegistered() + "'" +
            ", availability='" + getAvailability() + "'" +
            ", type='" + getType() + "'" +
            ", department='" + getDepartment() + "'" +
            "}";
    }
}
