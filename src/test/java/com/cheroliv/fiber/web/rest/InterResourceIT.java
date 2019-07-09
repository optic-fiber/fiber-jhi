package com.cheroliv.fiber.web.rest;

import com.cheroliv.fiber.FiberApp;
import com.cheroliv.fiber.domain.Inter;
import com.cheroliv.fiber.domain.Planning;
import com.cheroliv.fiber.repository.InterRepository;
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

import com.cheroliv.fiber.domain.enumeration.TypeInterEnum;
import com.cheroliv.fiber.domain.enumeration.ContractEnum;
/**
 * Integration tests for the {@Link InterResource} REST controller.
 */
@SpringBootTest(classes = FiberApp.class)
public class InterResourceIT {

    private static final String DEFAULT_ND = "AAAAAAAAAA";
    private static final String UPDATED_ND = "BBBBBBBBBB";

    private static final TypeInterEnum DEFAULT_TYPE_INTER = TypeInterEnum.BAAP;
    private static final TypeInterEnum UPDATED_TYPE_INTER = TypeInterEnum.BAOC;

    private static final ContractEnum DEFAULT_CONTRACT = ContractEnum.LM;
    private static final ContractEnum UPDATED_CONTRACT = ContractEnum.IQ;

    private static final String DEFAULT_LAST_NAME_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME_CLIENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_TIME_INTER = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME_INTER = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_COMPLEX = false;
    private static final Boolean UPDATED_COMPLEX = true;

    @Autowired
    private InterRepository interRepository;

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

    private MockMvc restInterMockMvc;

    private Inter inter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InterResource interResource = new InterResource(interRepository);
        this.restInterMockMvc = MockMvcBuilders.standaloneSetup(interResource)
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
    public static Inter createEntity(EntityManager em) {
        Inter inter = new Inter()
            .nd(DEFAULT_ND)
            .typeInter(DEFAULT_TYPE_INTER)
            .contract(DEFAULT_CONTRACT)
            .lastNameClient(DEFAULT_LAST_NAME_CLIENT)
            .firstNameClient(DEFAULT_FIRST_NAME_CLIENT)
            .dateTimeInter(DEFAULT_DATE_TIME_INTER)
            .complex(DEFAULT_COMPLEX);
        // Add required entity
        Planning planning;
        if (TestUtil.findAll(em, Planning.class).isEmpty()) {
            planning = PlanningResourceIT.createEntity(em);
            em.persist(planning);
            em.flush();
        } else {
            planning = TestUtil.findAll(em, Planning.class).get(0);
        }
        inter.setPlanning(planning);
        return inter;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inter createUpdatedEntity(EntityManager em) {
        Inter inter = new Inter()
            .nd(UPDATED_ND)
            .typeInter(UPDATED_TYPE_INTER)
            .contract(UPDATED_CONTRACT)
            .lastNameClient(UPDATED_LAST_NAME_CLIENT)
            .firstNameClient(UPDATED_FIRST_NAME_CLIENT)
            .dateTimeInter(UPDATED_DATE_TIME_INTER)
            .complex(UPDATED_COMPLEX);
        // Add required entity
        Planning planning;
        if (TestUtil.findAll(em, Planning.class).isEmpty()) {
            planning = PlanningResourceIT.createUpdatedEntity(em);
            em.persist(planning);
            em.flush();
        } else {
            planning = TestUtil.findAll(em, Planning.class).get(0);
        }
        inter.setPlanning(planning);
        return inter;
    }

    @BeforeEach
    public void initTest() {
        inter = createEntity(em);
    }

    @Test
    @Transactional
    public void createInter() throws Exception {
        int databaseSizeBeforeCreate = interRepository.findAll().size();

        // Create the Inter
        restInterMockMvc.perform(post("/api/inters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inter)))
            .andExpect(status().isCreated());

        // Validate the Inter in the database
        List<Inter> interList = interRepository.findAll();
        assertThat(interList).hasSize(databaseSizeBeforeCreate + 1);
        Inter testInter = interList.get(interList.size() - 1);
        assertThat(testInter.getNd()).isEqualTo(DEFAULT_ND);
        assertThat(testInter.getTypeInter()).isEqualTo(DEFAULT_TYPE_INTER);
        assertThat(testInter.getContract()).isEqualTo(DEFAULT_CONTRACT);
        assertThat(testInter.getLastNameClient()).isEqualTo(DEFAULT_LAST_NAME_CLIENT);
        assertThat(testInter.getFirstNameClient()).isEqualTo(DEFAULT_FIRST_NAME_CLIENT);
        assertThat(testInter.getDateTimeInter()).isEqualTo(DEFAULT_DATE_TIME_INTER);
        assertThat(testInter.isComplex()).isEqualTo(DEFAULT_COMPLEX);
    }

    @Test
    @Transactional
    public void createInterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interRepository.findAll().size();

        // Create the Inter with an existing ID
        inter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterMockMvc.perform(post("/api/inters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inter)))
            .andExpect(status().isBadRequest());

        // Validate the Inter in the database
        List<Inter> interList = interRepository.findAll();
        assertThat(interList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNdIsRequired() throws Exception {
        int databaseSizeBeforeTest = interRepository.findAll().size();
        // set the field null
        inter.setNd(null);

        // Create the Inter, which fails.

        restInterMockMvc.perform(post("/api/inters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inter)))
            .andExpect(status().isBadRequest());

        List<Inter> interList = interRepository.findAll();
        assertThat(interList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeInterIsRequired() throws Exception {
        int databaseSizeBeforeTest = interRepository.findAll().size();
        // set the field null
        inter.setTypeInter(null);

        // Create the Inter, which fails.

        restInterMockMvc.perform(post("/api/inters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inter)))
            .andExpect(status().isBadRequest());

        List<Inter> interList = interRepository.findAll();
        assertThat(interList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContractIsRequired() throws Exception {
        int databaseSizeBeforeTest = interRepository.findAll().size();
        // set the field null
        inter.setContract(null);

        // Create the Inter, which fails.

        restInterMockMvc.perform(post("/api/inters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inter)))
            .andExpect(status().isBadRequest());

        List<Inter> interList = interRepository.findAll();
        assertThat(interList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = interRepository.findAll().size();
        // set the field null
        inter.setLastNameClient(null);

        // Create the Inter, which fails.

        restInterMockMvc.perform(post("/api/inters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inter)))
            .andExpect(status().isBadRequest());

        List<Inter> interList = interRepository.findAll();
        assertThat(interList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateTimeInterIsRequired() throws Exception {
        int databaseSizeBeforeTest = interRepository.findAll().size();
        // set the field null
        inter.setDateTimeInter(null);

        // Create the Inter, which fails.

        restInterMockMvc.perform(post("/api/inters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inter)))
            .andExpect(status().isBadRequest());

        List<Inter> interList = interRepository.findAll();
        assertThat(interList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInters() throws Exception {
        // Initialize the database
        interRepository.saveAndFlush(inter);

        // Get all the interList
        restInterMockMvc.perform(get("/api/inters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inter.getId().intValue())))
            .andExpect(jsonPath("$.[*].nd").value(hasItem(DEFAULT_ND.toString())))
            .andExpect(jsonPath("$.[*].typeInter").value(hasItem(DEFAULT_TYPE_INTER.toString())))
            .andExpect(jsonPath("$.[*].contract").value(hasItem(DEFAULT_CONTRACT.toString())))
            .andExpect(jsonPath("$.[*].lastNameClient").value(hasItem(DEFAULT_LAST_NAME_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].firstNameClient").value(hasItem(DEFAULT_FIRST_NAME_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].dateTimeInter").value(hasItem(sameInstant(DEFAULT_DATE_TIME_INTER))))
            .andExpect(jsonPath("$.[*].complex").value(hasItem(DEFAULT_COMPLEX.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getInter() throws Exception {
        // Initialize the database
        interRepository.saveAndFlush(inter);

        // Get the inter
        restInterMockMvc.perform(get("/api/inters/{id}", inter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inter.getId().intValue()))
            .andExpect(jsonPath("$.nd").value(DEFAULT_ND.toString()))
            .andExpect(jsonPath("$.typeInter").value(DEFAULT_TYPE_INTER.toString()))
            .andExpect(jsonPath("$.contract").value(DEFAULT_CONTRACT.toString()))
            .andExpect(jsonPath("$.lastNameClient").value(DEFAULT_LAST_NAME_CLIENT.toString()))
            .andExpect(jsonPath("$.firstNameClient").value(DEFAULT_FIRST_NAME_CLIENT.toString()))
            .andExpect(jsonPath("$.dateTimeInter").value(sameInstant(DEFAULT_DATE_TIME_INTER)))
            .andExpect(jsonPath("$.complex").value(DEFAULT_COMPLEX.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInter() throws Exception {
        // Get the inter
        restInterMockMvc.perform(get("/api/inters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInter() throws Exception {
        // Initialize the database
        interRepository.saveAndFlush(inter);

        int databaseSizeBeforeUpdate = interRepository.findAll().size();

        // Update the inter
        Inter updatedInter = interRepository.findById(inter.getId()).get();
        // Disconnect from session so that the updates on updatedInter are not directly saved in db
        em.detach(updatedInter);
        updatedInter
            .nd(UPDATED_ND)
            .typeInter(UPDATED_TYPE_INTER)
            .contract(UPDATED_CONTRACT)
            .lastNameClient(UPDATED_LAST_NAME_CLIENT)
            .firstNameClient(UPDATED_FIRST_NAME_CLIENT)
            .dateTimeInter(UPDATED_DATE_TIME_INTER)
            .complex(UPDATED_COMPLEX);

        restInterMockMvc.perform(put("/api/inters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInter)))
            .andExpect(status().isOk());

        // Validate the Inter in the database
        List<Inter> interList = interRepository.findAll();
        assertThat(interList).hasSize(databaseSizeBeforeUpdate);
        Inter testInter = interList.get(interList.size() - 1);
        assertThat(testInter.getNd()).isEqualTo(UPDATED_ND);
        assertThat(testInter.getTypeInter()).isEqualTo(UPDATED_TYPE_INTER);
        assertThat(testInter.getContract()).isEqualTo(UPDATED_CONTRACT);
        assertThat(testInter.getLastNameClient()).isEqualTo(UPDATED_LAST_NAME_CLIENT);
        assertThat(testInter.getFirstNameClient()).isEqualTo(UPDATED_FIRST_NAME_CLIENT);
        assertThat(testInter.getDateTimeInter()).isEqualTo(UPDATED_DATE_TIME_INTER);
        assertThat(testInter.isComplex()).isEqualTo(UPDATED_COMPLEX);
    }

    @Test
    @Transactional
    public void updateNonExistingInter() throws Exception {
        int databaseSizeBeforeUpdate = interRepository.findAll().size();

        // Create the Inter

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterMockMvc.perform(put("/api/inters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inter)))
            .andExpect(status().isBadRequest());

        // Validate the Inter in the database
        List<Inter> interList = interRepository.findAll();
        assertThat(interList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInter() throws Exception {
        // Initialize the database
        interRepository.saveAndFlush(inter);

        int databaseSizeBeforeDelete = interRepository.findAll().size();

        // Delete the inter
        restInterMockMvc.perform(delete("/api/inters/{id}", inter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inter> interList = interRepository.findAll();
        assertThat(interList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inter.class);
        Inter inter1 = new Inter();
        inter1.setId(1L);
        Inter inter2 = new Inter();
        inter2.setId(inter1.getId());
        assertThat(inter1).isEqualTo(inter2);
        inter2.setId(2L);
        assertThat(inter1).isNotEqualTo(inter2);
        inter1.setId(null);
        assertThat(inter1).isNotEqualTo(inter2);
    }
}
