package pl.gontarczyk.studentmanagement.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import pl.gontarczyk.studentmanagement.common.Language;
import pl.gontarczyk.studentmanagement.student.model.Student;
import pl.gontarczyk.studentmanagement.teacher.TeacherRepository;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

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
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void init() {
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Dariusz")
                .lastName("Gontarczyk")
                .languages(Set.of(Language.JAVA, Language.KOTLIN))
                .active(true)
                .build();

        teacherRepository.save(teacher);
        studentRepository.save(Student.builder()
                        .id(1)
                        .firstName("Konrad")
                        .lastName("Gontarczyk")
                        .language(Language.JAVA)
                        .teacher(teacher)
                        .active(true)
                .build());
    }

    @Test
    void testFindAll_shouldReturnListWithStudents() throws Exception {
        mockMvc.perform(get("/api/v1/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.[0].firstName", equalTo("Konrad")))
                .andExpect(jsonPath("$.[0].lastName", equalTo("Gontarczyk")));
    }

    @Test
    void testSave_shouldSaveNewStudentInDatabase() throws Exception {
        int studentId = 2;
        assertTrue(studentRepository.findById(studentId).isEmpty());

        mockMvc.perform(post("/api/v1/students")
                        .param("teacherId", "1")
                        .content(objectMapper
                                .writeValueAsString(Student.builder()
                                        .id(studentId)
                                        .firstName("Krzysztof")
                                        .lastName("Gontarczyk")
                                        .language(Language.JAVA)
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo("Krzysztof")))
                .andExpect(jsonPath("$.lastName", equalTo("Gontarczyk")))
                .andExpect(jsonPath("$.language", equalTo("JAVA")));

        assertFalse(studentRepository.findById(studentId).isEmpty());
    }

    @Test
    void testFindById_shouldReturnStudentByIdFromDatabase() throws Exception {
        int studentId = 1;

        mockMvc.perform(get("/api/v1/students/" + studentId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Konrad")))
                .andExpect(jsonPath("$.lastName", equalTo("Gontarczyk")))
                .andExpect(jsonPath("$.language", equalTo("JAVA")));
    }

    @Test
    void testFindById_shouldThrowExceptionWhenStudentNotFoundInDatabase() throws Exception {
        int studentId = 10;
        String exceptionMsg = "Student not found!";

        mockMvc.perform(get("/api/v1/students/" + studentId, exceptionMsg))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(exceptionMsg, result.getResolvedException().getMessage()));
    }

    @Test
    void testDelete() throws Exception {
        int studentId = 1;
        assertFalse(studentRepository.findByIdAndActiveTrue(studentId).isEmpty());

        mockMvc.perform(delete("/api/v1/students/" + studentId))
                .andDo(print())
                .andExpect(status().isOk());

        assertTrue(studentRepository.findByIdAndActiveTrue(studentId).isEmpty());
    }

    @AfterEach
    void teardown() {
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
    }
}