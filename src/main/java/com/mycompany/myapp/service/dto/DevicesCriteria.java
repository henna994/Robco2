package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Devices entity. This class is used in DevicesResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /devices?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DevicesCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter model;

    private StringFilter registered;

    private StringFilter availability;

    private StringFilter type;

    private StringFilter department;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getModel() {
        return model;
    }

    public void setModel(IntegerFilter model) {
        this.model = model;
    }

    public StringFilter getRegistered() {
        return registered;
    }

    public void setRegistered(StringFilter registered) {
        this.registered = registered;
    }

    public StringFilter getAvailability() {
        return availability;
    }

    public void setAvailability(StringFilter availability) {
        this.availability = availability;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getDepartment() {
        return department;
    }

    public void setDepartment(StringFilter department) {
        this.department = department;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DevicesCriteria that = (DevicesCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(model, that.model) &&
            Objects.equals(registered, that.registered) &&
            Objects.equals(availability, that.availability) &&
            Objects.equals(type, that.type) &&
            Objects.equals(department, that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        model,
        registered,
        availability,
        type,
        department
        );
    }

    @Override
    public String toString() {
        return "DevicesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (model != null ? "model=" + model + ", " : "") +
                (registered != null ? "registered=" + registered + ", " : "") +
                (availability != null ? "availability=" + availability + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (department != null ? "department=" + department + ", " : "") +
            "}";
    }

}
