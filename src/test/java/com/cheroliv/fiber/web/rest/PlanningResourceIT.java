package com.cheroliv.fiber.web.rest;

import com.cheroliv.fiber.FiberApp;
import com.cheroliv.fiber.domain.Planning;
import com.cheroliv.fiber.domain.User;
import com.cheroliv.fiber.repository.PlanningRepository;
import com.cheroliv.fiber.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.cheroliv.fiber.web.rest.TestUtil.sameInstant;
import static com.cheroliv.fiber.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link PlanningResource} REST controller.
 */
@SpringBootTest(classes = FiberApp.class)
public class PlanningResourceIT {

    private static final String DEFAULT_INITIAL_TECH = "AAAAAAAAAA";
    private static final String UPDATED_INITIAL_TECH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OPEN = false;
    private static final Boolean UPDATED_OPEN = true;

    private static final ZonedDateTime DEFAULT_DATE_TIME_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LAST_NAME_TECH = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME_TECH = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME_TECH = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME_TECH = "BBBBBBBBBB";

    @Autowired
    private PlanningRepository planningRepository;

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

    private MockMvc restPlanningMockMvc;

    private Planning planning;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanningResource planningResource = new PlanningResource(planningRepository);
        this.restPlanningMockMvc = MockMvcBuilders.standaloneSetup(planningResource)
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
    public static Planning createEntity(EntityManager em) {
        Planning planning = new Planning()
            .initialTech(DEFAULT_INITIAL_TECH)
            .open(DEFAULT_OPEN)
            .dateTimeCreation(DEFAULT_DATE_TIME_CREATION)
            .lastNameTech(DEFAULT_LAST_NAME_TECH)
            .firstNameTech(DEFAULT_FIRST_NAME_TECH);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        planning.setUser(user);
        return planning;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planning createUpdatedEntity(EntityManager em) {
        Planning planning = new Planning()
            .initialTech(UPDATED_INITIAL_TECH)
            .open(UPDATED_OPEN)
            .dateTimeCreation(UPDATED_DATE_TIME_CREATION)
            .lastNameTech(UPDATED_LAST_NAME_TECH)
            .firstNameTech(UPDATED_FIRST_NAME_TECH);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        planning.setUser(user);
        return planning;
    }

    @BeforeEach
    public void initTest() {
        planning = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanning() throws Exception {
        int databaseSizeBeforeCreate = planningRepository.findAll().size();

        // Create the Planning
        restPlanningMockMvc.perform(post("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isCreated());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeCreate + 1);
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.getInitialTech()).isEqualTo(DEFAULT_INITIAL_TECH);
        assertThat(testPlanning.isOpen()).isEqualTo(DEFAULT_OPEN);
        assertThat(testPlanning.getDateTimeCreation()).isEqualTo(DEFAULT_DATE_TIME_CREATION);
        assertThat(testPlanning.getLastNameTech()).isEqualTo(DEFAULT_LAST_NAME_TECH);
        assertThat(testPlanning.getFirstNameTech()).isEqualTo(DEFAULT_FIRST_NAME_TECH);
    }

    @Test
    @Transactional
    public void createPlanningWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planningRepository.findAll().size();

        // Create the Planning with an existing ID
        planning.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanningMockMvc.perform(post("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkInitialTechIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningRepository.findAll().size();
        // set the field null
        planning.setInitialTech(null);

        // Create the Planning, which fails.

        restPlanningMockMvc.perform(post("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isBadRequest());

        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOpenIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningRepository.findAll().size();
        // set the field null
        planning.setOpen(null);

        // Create the Planning, which fails.

        restPlanningMockMvc.perform(post("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isBadRequest());

        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateTimeCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningRepository.findAll().size();
        // set the field null
        planning.setDateTimeCreation(null);

        // Create the Planning, which fails.

        restPlanningMockMvc.perform(post("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isBadRequest());

        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameTechIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningRepository.findAll().size();
        // set the field null
        planning.setLastNameTech(null);

        // Create the Planning, which fails.

        restPlanningMockMvc.perform(post("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isBadRequest());

        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlannings() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        // Get all the planningList
        restPlanningMockMvc.perform(get("/api/plannings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planning.getId().intValue())))
            .andExpect(jsonPath("$.[*].initialTech").value(hasItem(DEFAULT_INITIAL_TECH.toString())))
            .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateTimeCreation").value(hasItem(sameInstant(DEFAULT_DATE_TIME_CREATION))))
            .andExpect(jsonPath("$.[*].lastNameTech").value(hasItem(DEFAULT_LAST_NAME_TECH.toString())))
            .andExpect(jsonPath("$.[*].firstNameTech").value(hasItem(DEFAULT_FIRST_NAME_TECH.toString())));
    }
    
    @Test
    @Transactional
    public void getPlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        // Get the planning
        restPlanningMockMvc.perform(get("/api/plannings/{id}", planning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planning.getId().intValue()))
            .andExpect(jsonPath("$.initialTech").value(DEFAULT_INITIAL_TECH.toString()))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.booleanValue()))
            .andExpect(jsonPath("$.dateTimeCreation").value(sameInstant(DEFAULT_DATE_TIME_CREATION)))
            .andExpect(jsonPath("$.lastNameTech").value(DEFAULT_LAST_NAME_TECH.toString()))
            .andExpect(jsonPath("$.firstNameTech").value(DEFAULT_FIRST_NAME_TECH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlanning() throws Exception {
        // Get the planning
        restPlanningMockMvc.perform(get("/api/plannings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        int databaseSizeBeforeUpdate = planningRepository.findAll().size();

        // Update the planning
        Planning updatedPlanning = planningRepository.findById(planning.getId()).get();
        // Disconnect from session so that the updates on updatedPlanning are not directly saved in db
        em.detach(updatedPlanning);
        updatedPlanning
            .initialTech(UPDATED_INITIAL_TECH)
            .open(UPDATED_OPEN)
            .dateTimeCreation(UPDATED_DATE_TIME_CREATION)
            .lastNameTech(UPDATED_LAST_NAME_TECH)
            .firstNameTech(UPDATED_FIRST_NAME_TECH);

        restPlanningMockMvc.perform(put("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlanning)))
            .andExpect(status().isOk());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.getInitialTech()).isEqualTo(UPDATED_INITIAL_TECH);
        assertThat(testPlanning.isOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testPlanning.getDateTimeCreation()).isEqualTo(UPDATED_DATE_TIME_CREATION);
        assertThat(testPlanning.getLastNameTech()).isEqualTo(UPDATED_LAST_NAME_TECH);
        assertThat(testPlanning.getFirstNameTech()).isEqualTo(UPDATED_FIRST_NAME_TECH);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();

        // Create the Planning

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningMockMvc.perform(put("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        int databaseSizeBeforeDelete = planningRepository.findAll().size();

        // Delete the planning
        restPlanningMockMvc.perform(delete("/api/plannings/{id}", planning.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Planning.class);
        Planning planning1 = new Planning();
        planning1.setId(1L);
        Planning planning2 = new Planning();
        planning2.setId(planning1.getId());
        assertThat(planning1).isEqualTo(planning2);
        planning2.setId(2L);
        assertThat(planning1).isNotEqualTo(planning2);
        planning1.setId(null);
        assertThat(planning1).isNotEqualTo(planning2);
    }
}
