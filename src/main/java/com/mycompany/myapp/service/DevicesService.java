package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Devices;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Devices.
 */
public interface DevicesService {

    /**
     * Save a devices.
     *
     * @param devices the entity to save
     * @return the persisted entity
     */
    Devices save(Devices devices);

    /**
     * Get all the devices.
     *
     * @return the list of entities
     */
    List<Devices> findAll();


    /**
     * Get the "id" devices.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Devices> findOne(Long id);

    /**
     * Delete the "id" devices.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the devices corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Devices> search(String query);
}
