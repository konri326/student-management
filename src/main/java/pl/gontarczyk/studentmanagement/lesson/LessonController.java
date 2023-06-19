package pl.gontarczyk.studentmanagement.lesson;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gontarczyk.studentmanagement.lesson.mapper.LessonMapper;
import pl.gontarczyk.studentmanagement.lesson.model.Lesson;
import pl.gontarczyk.studentmanagement.lesson.model.LessonDto;
import pl.gontarczyk.studentmanagement.lesson.model.LessonViewDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    @GetMapping
    public ResponseEntity<List<LessonViewDto>> findAll() {
        List<LessonViewDto> lessons = lessonService.findAll().stream()
                .map(lessonMapper::toViewDto)
                .toList();
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @PostMapping(params = "studentId")
    public ResponseEntity<LessonViewDto> save(
            @RequestBody @Valid LessonDto lessonDto,
            @RequestParam("studentId") int studentId)
    {
        Lesson returnedLesson = lessonService.save(lessonMapper.toEntity(lessonDto), studentId);
        LessonViewDto lessonViewDto = lessonMapper.toViewDto(returnedLesson);
        return new ResponseEntity<>(lessonViewDto, HttpStatus.CREATED);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<LessonViewDto> update(@RequestBody @Valid LessonDto lessonDto, @PathVariable("id") int id) {
        Lesson returnedLesson = lessonService.update(lessonMapper.toEntity(lessonDto), id);
        LessonViewDto lessonViewDto = lessonMapper.toViewDto(returnedLesson);
        return new ResponseEntity<>(lessonViewDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonViewDto> findById(@PathVariable("id") int id) {
        LessonViewDto returnedLesson = lessonMapper.toViewDto(lessonService.findById(id));
        return new ResponseEntity<>(returnedLesson, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        lessonService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}