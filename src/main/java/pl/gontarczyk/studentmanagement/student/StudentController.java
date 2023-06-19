package pl.gontarczyk.studentmanagement.student;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gontarczyk.studentmanagement.student.mapper.StudentMapper;
import pl.gontarczyk.studentmanagement.student.model.Student;
import pl.gontarczyk.studentmanagement.student.model.StudentDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @GetMapping
    public ResponseEntity<List<StudentDto>> findAll() {
        List<StudentDto> students = studentService.findAll().stream()
                .map(studentMapper::toDto)
                .toList();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping(params = "teacherId")
    public ResponseEntity<StudentDto> save(
            @RequestBody @Valid StudentDto studentDto,
            @RequestParam("teacherId") int teacherId
    ) {
        Student student = studentService.save(studentMapper.toEntity(studentDto), teacherId);
        StudentDto returnedStudent = studentMapper.toDto(student);
        return new ResponseEntity<>(returnedStudent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> findById(@PathVariable("id") int id) {
        StudentDto returnedStudent = studentMapper.toDto(studentService.findById(id));
        return new ResponseEntity<>(returnedStudent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        studentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}