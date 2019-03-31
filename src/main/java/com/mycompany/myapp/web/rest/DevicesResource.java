package com.mycompany.myapp.web.rest;
import com.mycompany.myapp.domain.Devices;
import com.mycompany.myapp.service.DevicesService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.service.dto.DevicesCriteria;
import com.mycompany.myapp.service.DevicesQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Devices.
 */
@RestController
@RequestMapping("/api")
public class DevicesResource {

    private final Logger log = LoggerFactory.getLogger(DevicesResource.class);

    private static final String ENTITY_NAME = "devices";

    private final DevicesService devicesService;

    private final DevicesQueryService devicesQueryService;

    public DevicesResource(DevicesService devicesService, DevicesQueryService devicesQueryService) {
        this.devicesService = devicesService;
        this.devicesQueryService = devicesQueryService;
    }

    /**
     * POST  /devices : Create a new devices.
     *
     * @param devices the devices to create
     * @return the ResponseEntity with status 201 (Created) and with body the new devices, or with status 400 (Bad Request) if the devices has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/devices")
    public ResponseEntity<Devices> createDevices(@RequestBody Devices devices) throws URISyntaxException {
        log.debug("REST request to save Devices : {}", devices);
        if (devices.getId() != null) {
            throw new BadRequestAlertException("A new devices cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Devices result = devicesService.save(devices);
        return ResponseEntity.created(new URI("/api/devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /devices : Updates an existing devices.
     *
     * @param devices the devices to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated devices,
     * or with status 400 (Bad Request) if the devices is not valid,
     * or with status 500 (Internal Server Error) if the devices couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/devices")
    public ResponseEntity<Devices> updateDevices(@RequestBody Devices devices) throws URISyntaxException {
        log.debug("REST request to update Devices : {}", devices);
        if (devices.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Devices result = devicesService.save(devices);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, devices.getId().toString()))
            .body(result);
    }

    /**
     * GET  /devices : get all the devices.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of devices in body
     */
    @GetMapping("/devices")
    public ResponseEntity<List<Devices>> getAllDevices(DevicesCriteria criteria) {
        log.debug("REST request to get Devices by criteria: {}", criteria);
        List<Devices> entityList = devicesQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /devices/count : count all the devices.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/devices/count")
    public ResponseEntity<Long> countDevices(DevicesCriteria criteria) {
        log.debug("REST request to count Devices by criteria: {}", criteria);
        return ResponseEntity.ok().body(devicesQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /devices/:id : get the "id" devices.
     *
     * @param id the id of the devices to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the devices, or with status 404 (Not Found)
     */
    @GetMapping("/devices/{id}")
    public ResponseEntity<Devices> getDevices(@PathVariable Long id) {
        log.debug("REST request to get Devices : {}", id);
        Optional<Devices> devices = devicesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(devices);
    }

    /**
     * DELETE  /devices/:id : delete the "id" devices.
     *
     * @param id the id of the devices to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> deleteDevices(@PathVariable Long id) {
        log.debug("REST request to delete Devices : {}", id);
        devicesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/devices?query=:query : search for the devices corresponding
     * to the query.
     *
     * @param query the query of the devices search
     * @return the result of the search
     */
    @GetMapping("/_search/devices")
    public List<Devices> searchDevices(@RequestParam String query) {
        log.debug("REST request to search Devices for query {}", query);
        return devicesService.search(query);
    }

}
