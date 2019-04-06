package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RobcoApp;

import com.mycompany.myapp.domain.Devices;
import com.mycompany.myapp.repository.DevicesRepository;
import com.mycompany.myapp.repository.search.DevicesSearchRepository;
import com.mycompany.myapp.service.DevicesService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.DevicesCriteria;
import com.mycompany.myapp.service.DevicesQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DevicesResource REST controller.
 *
 * @see DevicesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RobcoApp.class)
public class DevicesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_MODEL = 1;
    private static final Integer UPDATED_MODEL = 2;

    private static final String DEFAULT_REGISTERED = "AAAAAAAAAA";
    private static final String UPDATED_REGISTERED = "BBBBBBBBBB";

    private static final String DEFAULT_AVAILABILITY = "AAAAAAAAAA";
    private static final String UPDATED_AVAILABILITY = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private DevicesService devicesService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.DevicesSearchRepositoryMockConfiguration
     */
    @Autowired
    private DevicesSearchRepository mockDevicesSearchRepository;

    @Autowired
    private DevicesQueryService devicesQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDevicesMockMvc;

    private Devices devices;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DevicesResource devicesResource = new DevicesResource(devicesService, devicesQueryService);
        this.restDevicesMockMvc = MockMvcBuilders.standaloneSetup(devicesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Devices createEntity(EntityManager em) {
        Devices devices = new Devices()
            .name(DEFAULT_NAME)
            .model(DEFAULT_MODEL)
            .registered(DEFAULT_REGISTERED)
            .availability(DEFAULT_AVAILABILITY)
            .type(DEFAULT_TYPE)
            .department(DEFAULT_DEPARTMENT);
        return devices;
    }

    @Before
    public void initTest() {
        devices = createEntity(em);
    }

    @Test
    @Transactional
    public void createDevices() throws Exception {
        int databaseSizeBeforeCreate = devicesRepository.findAll().size();

        // Create the Devices
        restDevicesMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devices)))
            .andExpect(status().isCreated());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeCreate + 1);
        Devices testDevices = devicesList.get(devicesList.size() - 1);
        assertThat(testDevices.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDevices.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testDevices.getRegistered()).isEqualTo(DEFAULT_REGISTERED);
        assertThat(testDevices.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
        assertThat(testDevices.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDevices.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(1)).save(testDevices);
    }

    @Test
    @Transactional
    public void createDevicesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = devicesRepository.findAll().size();

        // Create the Devices with an existing ID
        devices.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDevicesMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devices)))
            .andExpect(status().isBadRequest());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(0)).save(devices);
    }

    @Test
    @Transactional
    public void getAllDevices() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList
        restDevicesMockMvc.perform(get("/api/devices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(devices.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].registered").value(hasItem(DEFAULT_REGISTERED.toString())))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT.toString())));
    }
    
    @Test
    @Transactional
    public void getDevices() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get the devices
        restDevicesMockMvc.perform(get("/api/devices/{id}", devices.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(devices.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.registered").value(DEFAULT_REGISTERED.toString()))
            .andExpect(jsonPath("$.availability").value(DEFAULT_AVAILABILITY.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT.toString()));
    }

    @Test
    @Transactional
    public void getAllDevicesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where name equals to DEFAULT_NAME
        defaultDevicesShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the devicesList where name equals to UPDATED_NAME
        defaultDevicesShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDevicesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDevicesShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the devicesList where name equals to UPDATED_NAME
        defaultDevicesShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDevicesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where name is not null
        defaultDevicesShouldBeFound("name.specified=true");

        // Get all the devicesList where name is null
        defaultDevicesShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllDevicesByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where model equals to DEFAULT_MODEL
        defaultDevicesShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the devicesList where model equals to UPDATED_MODEL
        defaultDevicesShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void getAllDevicesByModelIsInShouldWork() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultDevicesShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the devicesList where model equals to UPDATED_MODEL
        defaultDevicesShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void getAllDevicesByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where model is not null
        defaultDevicesShouldBeFound("model.specified=true");

        // Get all the devicesList where model is null
        defaultDevicesShouldNotBeFound("model.specified=false");
    }

    @Test
    @Transactional
    public void getAllDevicesByModelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where model greater than or equals to DEFAULT_MODEL
        defaultDevicesShouldBeFound("model.greaterOrEqualThan=" + DEFAULT_MODEL);

        // Get all the devicesList where model greater than or equals to UPDATED_MODEL
        defaultDevicesShouldNotBeFound("model.greaterOrEqualThan=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void getAllDevicesByModelIsLessThanSomething() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where model less than or equals to DEFAULT_MODEL
        defaultDevicesShouldNotBeFound("model.lessThan=" + DEFAULT_MODEL);

        // Get all the devicesList where model less than or equals to UPDATED_MODEL
        defaultDevicesShouldBeFound("model.lessThan=" + UPDATED_MODEL);
    }


    @Test
    @Transactional
    public void getAllDevicesByRegisteredIsEqualToSomething() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where registered equals to DEFAULT_REGISTERED
        defaultDevicesShouldBeFound("registered.equals=" + DEFAULT_REGISTERED);

        // Get all the devicesList where registered equals to UPDATED_REGISTERED
        defaultDevicesShouldNotBeFound("registered.equals=" + UPDATED_REGISTERED);
    }

    @Test
    @Transactional
    public void getAllDevicesByRegisteredIsInShouldWork() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where registered in DEFAULT_REGISTERED or UPDATED_REGISTERED
        defaultDevicesShouldBeFound("registered.in=" + DEFAULT_REGISTERED + "," + UPDATED_REGISTERED);

        // Get all the devicesList where registered equals to UPDATED_REGISTERED
        defaultDevicesShouldNotBeFound("registered.in=" + UPDATED_REGISTERED);
    }

    @Test
    @Transactional
    public void getAllDevicesByRegisteredIsNullOrNotNull() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where registered is not null
        defaultDevicesShouldBeFound("registered.specified=true");

        // Get all the devicesList where registered is null
        defaultDevicesShouldNotBeFound("registered.specified=false");
    }

    @Test
    @Transactional
    public void getAllDevicesByAvailabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where availability equals to DEFAULT_AVAILABILITY
        defaultDevicesShouldBeFound("availability.equals=" + DEFAULT_AVAILABILITY);

        // Get all the devicesList where availability equals to UPDATED_AVAILABILITY
        defaultDevicesShouldNotBeFound("availability.equals=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    public void getAllDevicesByAvailabilityIsInShouldWork() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where availability in DEFAULT_AVAILABILITY or UPDATED_AVAILABILITY
        defaultDevicesShouldBeFound("availability.in=" + DEFAULT_AVAILABILITY + "," + UPDATED_AVAILABILITY);

        // Get all the devicesList where availability equals to UPDATED_AVAILABILITY
        defaultDevicesShouldNotBeFound("availability.in=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    public void getAllDevicesByAvailabilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where availability is not null
        defaultDevicesShouldBeFound("availability.specified=true");

        // Get all the devicesList where availability is null
        defaultDevicesShouldNotBeFound("availability.specified=false");
    }

    @Test
    @Transactional
    public void getAllDevicesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where type equals to DEFAULT_TYPE
        defaultDevicesShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the devicesList where type equals to UPDATED_TYPE
        defaultDevicesShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDevicesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultDevicesShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the devicesList where type equals to UPDATED_TYPE
        defaultDevicesShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDevicesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where type is not null
        defaultDevicesShouldBeFound("type.specified=true");

        // Get all the devicesList where type is null
        defaultDevicesShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllDevicesByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where department equals to DEFAULT_DEPARTMENT
        defaultDevicesShouldBeFound("department.equals=" + DEFAULT_DEPARTMENT);

        // Get all the devicesList where department equals to UPDATED_DEPARTMENT
        defaultDevicesShouldNotBeFound("department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllDevicesByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where department in DEFAULT_DEPARTMENT or UPDATED_DEPARTMENT
        defaultDevicesShouldBeFound("department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT);

        // Get all the devicesList where department equals to UPDATED_DEPARTMENT
        defaultDevicesShouldNotBeFound("department.in=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllDevicesByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList where department is not null
        defaultDevicesShouldBeFound("department.specified=true");

        // Get all the devicesList where department is null
        defaultDevicesShouldNotBeFound("department.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDevicesShouldBeFound(String filter) throws Exception {
        restDevicesMockMvc.perform(get("/api/devices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(devices.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].registered").value(hasItem(DEFAULT_REGISTERED)))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)));

        // Check, that the count call also returns 1
        restDevicesMockMvc.perform(get("/api/devices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDevicesShouldNotBeFound(String filter) throws Exception {
        restDevicesMockMvc.perform(get("/api/devices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDevicesMockMvc.perform(get("/api/devices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDevices() throws Exception {
        // Get the devices
        restDevicesMockMvc.perform(get("/api/devices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDevices() throws Exception {
        // Initialize the database
        devicesService.save(devices);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDevicesSearchRepository);

        int databaseSizeBeforeUpdate = devicesRepository.findAll().size();

        // Update the devices
        Devices updatedDevices = devicesRepository.findById(devices.getId()).get();
        // Disconnect from session so that the updates on updatedDevices are not directly saved in db
        em.detach(updatedDevices);
        updatedDevices
            .name(UPDATED_NAME)
            .model(UPDATED_MODEL)
            .registered(UPDATED_REGISTERED)
            .availability(UPDATED_AVAILABILITY)
            .type(UPDATED_TYPE)
            .department(UPDATED_DEPARTMENT);

        restDevicesMockMvc.perform(put("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDevices)))
            .andExpect(status().isOk());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate);
        Devices testDevices = devicesList.get(devicesList.size() - 1);
        assertThat(testDevices.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDevices.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testDevices.getRegistered()).isEqualTo(UPDATED_REGISTERED);
        assertThat(testDevices.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        assertThat(testDevices.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDevices.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(1)).save(testDevices);
    }

    @Test
    @Transactional
    public void updateNonExistingDevices() throws Exception {
        int databaseSizeBeforeUpdate = devicesRepository.findAll().size();

        // Create the Devices

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDevicesMockMvc.perform(put("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devices)))
            .andExpect(status().isBadRequest());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(0)).save(devices);
    }

    @Test
    @Transactional
    public void deleteDevices() throws Exception {
        // Initialize the database
        devicesService.save(devices);

        int databaseSizeBeforeDelete = devicesRepository.findAll().size();

        // Delete the devices
        restDevicesMockMvc.perform(delete("/api/devices/{id}", devices.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(1)).deleteById(devices.getId());
    }

    @Test
    @Transactional
    public void searchDevices() throws Exception {
        // Initialize the database
        devicesService.save(devices);
        when(mockDevicesSearchRepository.search(queryStringQuery("id:" + devices.getId())))
            .thenReturn(Collections.singletonList(devices));
        // Search the devices
        restDevicesMockMvc.perform(get("/api/_search/devices?query=id:" + devices.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(devices.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].registered").value(hasItem(DEFAULT_REGISTERED)))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Devices.class);
        Devices devices1 = new Devices();
        devices1.setId(1L);
        Devices devices2 = new Devices();
        devices2.setId(devices1.getId());
        assertThat(devices1).isEqualTo(devices2);
        devices2.setId(2L);
        assertThat(devices1).isNotEqualTo(devices2);
        devices1.setId(null);
        assertThat(devices1).isNotEqualTo(devices2);
    }
}
