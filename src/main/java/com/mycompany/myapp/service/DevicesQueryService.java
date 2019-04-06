package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Devices;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.DevicesRepository;
import com.mycompany.myapp.repository.search.DevicesSearchRepository;
import com.mycompany.myapp.service.dto.DevicesCriteria;

/**
 * Service for executing complex queries for Devices entities in the database.
 * The main input is a {@link DevicesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Devices} or a {@link Page} of {@link Devices} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DevicesQueryService extends QueryService<Devices> {

    private final Logger log = LoggerFactory.getLogger(DevicesQueryService.class);

    private final DevicesRepository devicesRepository;

    private final DevicesSearchRepository devicesSearchRepository;

    public DevicesQueryService(DevicesRepository devicesRepository, DevicesSearchRepository devicesSearchRepository) {
        this.devicesRepository = devicesRepository;
        this.devicesSearchRepository = devicesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Devices} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Devices> findByCriteria(DevicesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Devices> specification = createSpecification(criteria);
        return devicesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Devices} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Devices> findByCriteria(DevicesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Devices> specification = createSpecification(criteria);
        return devicesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DevicesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Devices> specification = createSpecification(criteria);
        return devicesRepository.count(specification);
    }

    /**
     * Function to convert DevicesCriteria to a {@link Specification}
     */
    private Specification<Devices> createSpecification(DevicesCriteria criteria) {
        Specification<Devices> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Devices_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Devices_.name));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModel(), Devices_.model));
            }
            if (criteria.getRegistered() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRegistered(), Devices_.registered));
            }
            if (criteria.getAvailability() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAvailability(), Devices_.availability));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Devices_.type));
            }
            if (criteria.getDepartment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepartment(), Devices_.department));
            }
        }
        return specification;
    }
}
