package pl.gontarczyk.studentmanagement.teacher;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gontarczyk.studentmanagement.teacher.mapper.TeacherMapper;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;
import pl.gontarczyk.studentmanagement.teacher.model.TeacherDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    @GetMapping
    public ResponseEntity<List<TeacherDto>> findAll() {
        List<TeacherDto> teachers = teacherService.findAll().stream()
                .map(teacherMapper::toDto)
                .toList();
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TeacherDto> save(@RequestBody @Valid TeacherDto teacherDto) {
        Teacher teacher = teacherService.save(teacherMapper.toEntity(teacherDto));
        TeacherDto returnedTeacher = teacherMapper.toDto(teacher);
        return new ResponseEntity<>(returnedTeacher, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> findById(@PathVariable("id") int id) {
        TeacherDto returnedTeacher = teacherMapper.toDto(teacherService.findById(id));
        return new ResponseEntity<>(returnedTeacher, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") int id) {
        teacherService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}