package pl.gontarczyk.studentmanagement.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.gontarczyk.studentmanagement.common.Language;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({" test "})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    void init() {
        teacherRepository.saveAll(List.of(
                Teacher.builder()
                        .id(1)
                        .firstName("Dariusz")
                        .lastName("Gontarczyk")
                        .languages(Set.of(Language.JAVA, Language.KOTLIN))
                        .active(true)
                        .build(),
                Teacher.builder()
                        .id(2)
                        .firstName("Krzysztof")
                        .lastName("Gontarczyk")
                        .languages(Set.of(Language.JAVA))
                        .active(true)
                        .build()
        ));
    }

    @Test
    void testFindAll_shouldReturnListWithTeachers() throws Exception {
        mockMvc.perform(get("/api/v1/teachers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.[0].firstName", equalTo("Dariusz")))
                .andExpect(jsonPath("$.[1].firstName", equalTo("Krzysztof")))
                .andExpect(jsonPath("$.[0].lastName", equalTo("Gontarczyk")))
                .andExpect(jsonPath("$.[1].lastName", equalTo("Gontarczyk")));
    }

    @Test
    void testSave_shouldSaveNewTeacherInDatabase() throws Exception {
        int teacherId = 3;
        assertTrue(teacherRepository.findById(teacherId).isEmpty());

        mockMvc.perform(post("/api/v1/teachers")
                        .content(objectMapper
                                .writeValueAsString(Teacher.builder()
                                        .id(teacherId)
                                        .firstName("Konrad")
                                        .lastName("Gontarczyk")
                                        .languages(Set.of(Language.PYTHON))
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo("Konrad")))
                .andExpect(jsonPath("$.lastName", equalTo("Gontarczyk")))
                .andExpect(jsonPath("$.languages").isNotEmpty());

        assertFalse(teacherRepository.findById(teacherId).isEmpty());
    }

    @Test
    void testFindById_shouldReturnTeacherByIdFromDatabase() throws Exception {
        int teacherId = 1;

        mockMvc.perform(get("/api/v1/teachers/" + teacherId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Dariusz")))
                .andExpect(jsonPath("$.lastName", equalTo("Gontarczyk")))
                .andExpect(jsonPath("$.languages").isNotEmpty());
    }

    @Test
    void testFindById_shouldThrowExceptionWhenTeacherNotFoundInDatabase() throws Exception {
        int teacherId = 10;
        String exceptionMsg = "Teacher not found!";

        mockMvc.perform(get("/api/v1/teachers/" + teacherId, exceptionMsg))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(exceptionMsg, result.getResolvedException().getMessage()));
    }

    @Test
    void testDeleteById_shouldChangeFieldActiveInTeacherOnFalse() throws Exception {
        int teacherId = 2;
        assertFalse(teacherRepository.findByIdAndActiveTrue(teacherId).isEmpty());

        mockMvc.perform(delete("/api/v1/teachers/" + teacherId))
                .andDo(print())
                .andExpect(status().isOk());

        assertTrue(teacherRepository.findByIdAndActiveTrue(teacherId).isEmpty());
    }

    @AfterEach
    void teardown() {
        teacherRepository.deleteAll();
    }
}