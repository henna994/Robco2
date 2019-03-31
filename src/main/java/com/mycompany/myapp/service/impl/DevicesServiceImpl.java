package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.DevicesService;
import com.mycompany.myapp.domain.Devices;
import com.mycompany.myapp.repository.DevicesRepository;
import com.mycompany.myapp.repository.search.DevicesSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Devices.
 */
@Service
@Transactional
public class DevicesServiceImpl implements DevicesService {

    private final Logger log = LoggerFactory.getLogger(DevicesServiceImpl.class);

    private final DevicesRepository devicesRepository;

    private final DevicesSearchRepository devicesSearchRepository;

    public DevicesServiceImpl(DevicesRepository devicesRepository, DevicesSearchRepository devicesSearchRepository) {
        this.devicesRepository = devicesRepository;
        this.devicesSearchRepository = devicesSearchRepository;
    }

    /**
     * Save a devices.
     *
     * @param devices the entity to save
     * @return the persisted entity
     */
    @Override
    public Devices save(Devices devices) {
        log.debug("Request to save Devices : {}", devices);
        Devices result = devicesRepository.save(devices);
        devicesSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the devices.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Devices> findAll() {
        log.debug("Request to get all Devices");
        return devicesRepository.findAll();
    }


    /**
     * Get one devices by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Devices> findOne(Long id) {
        log.debug("Request to get Devices : {}", id);
        return devicesRepository.findById(id);
    }

    /**
     * Delete the devices by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Devices : {}", id);
        devicesRepository.deleteById(id);
        devicesSearchRepository.deleteById(id);
    }

    /**
     * Search for the devices corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Devices> search(String query) {
        log.debug("Request to search Devices for query {}", query);
        return StreamSupport
            .stream(devicesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
