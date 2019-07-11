package com.cheroliv.fiber.config

import com.cheroliv.fiber.domain.Inter
import com.cheroliv.fiber.domain.Planning
import com.cheroliv.fiber.domain.enumeration.ContractEnum
import com.cheroliv.fiber.domain.enumeration.TypeInterEnum
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.*
import org.springframework.test.context.ActiveProfiles

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Slf4j
@CompileStatic
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation)
class DatabaseInitializerTest {

    static Validator validator


    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    synchronized static LocalTime parseStringHeureToLocalTime(String strHeure) {
        LocalTime.of(Integer.parseInt("${strHeure.charAt(0)}${strHeure.charAt(1)}"), 0)
    }

    synchronized static LocalDate parseStringDateToLocalDate(String strDate) {
        LocalDate.parse(strDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    synchronized static ZonedDateTime getDateTime(LocalDate date, LocalTime time, ZoneId zoneId) {
        ZonedDateTime.of(
            date,
            time,
            zoneId)
    }

    @Order(1)
    @Test
    @DisplayName("Test insert inter.json")
    void testProperJsonFileExistsOrBuildIt() {
        log.info("test #1")
        String strPath = FileSystems.getDefault().getPath(".").toAbsolutePath().toString()
        String jsonInterFilePath = StringUtils.chop(strPath)
        jsonInterFilePath = jsonInterFilePath + "src/test/resources/inter_.json"
        Object jsonInters = new JsonSlurper().parse(new File(jsonInterFilePath))
        List<Inter> inters = new ArrayList<>()
        jsonInters.each { Map<String, String> it ->
            Inter inter = new Inter(
                id: Long.valueOf(it["id"]),
                nd: it["nd"],
                typeInter: TypeInterEnum.valueOf(it["typeInter"]),
                contract: ContractEnum.valueOf(it["contract"]),
                complex: false,
                dateTimeInter: getDateTime(
                    parseStringDateToLocalDate(it["date"] as String),
                    parseStringHeureToLocalTime(it["hour"] as String),
                    ZoneId.systemDefault()),
                lastNameClient: it["lastNameClient"] as String,
                firstNameClient: it["firstNameClient"] as String,
            )
            inters.add(inter)
        }


        String jsonFileZonedPath = StringUtils.chop(strPath) + "src/test/resources/inter.json"

        File jsonFileZoned = new File(jsonFileZonedPath)
        if (!jsonFileZoned.exists()) {
            jsonFileZoned.createNewFile()
        } else {
            !(jsonFileZoned.exists() && jsonFileZoned.directory) ?: jsonFileZoned.delete()
        }
        ObjectMapper mapper = new ObjectMapper()
        mapper.registerModule(new JavaTimeModule())
        jsonFileZoned.setText("[", StandardCharsets.UTF_8.toString())
        int size = inters.size()
        inters.eachWithIndex { Inter it, int idx ->
            String text = mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(it)
            if (idx < size - 1) {
                text = "${text},\n"
            }
            jsonFileZoned.append(
                text,
                StandardCharsets.UTF_8.toString())
        }
        jsonFileZoned.append(
            "]",
            StandardCharsets.UTF_8.toString())

    }


    @Order(2)
    @Test
    @DisplayName("test DatabaseInitializer.loadInters() logic")
    void test_interjsonFile_isValidData() {
        log.info("test #2")
        String strPath = FileSystems.getDefault().getPath(".").toAbsolutePath().toString()
        String jsonInterFilePath = StringUtils.chop(strPath)
        jsonInterFilePath = jsonInterFilePath + "src/test/resources/inter.json"

        Planning planning = new Planning()
        File jsonFile = new File(jsonInterFilePath)
        ObjectMapper mapper = new ObjectMapper()
        mapper.registerModule(new JavaTimeModule())
        List<Inter> inters = mapper.readValue(
            jsonFile.text,
            new TypeReference<List<Inter>>() {})
        Set<ConstraintViolation<Inter>> constraintViolations
        inters.each { Inter it ->
            it.id = null
            it.planning = planning
            constraintViolations = validator.validate(it)
            assert constraintViolations.empty
        }
    }
}
