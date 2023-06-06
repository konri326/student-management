package pl.gontarczyk.studentmanagement.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.gontarczyk.studentmanagement.teacher.mapper.TeacherMapper;
import pl.gontarczyk.studentmanagement.teacher.mapper.TeacherMapperImpl;

@Configuration
public class MapStructConfiguration {

    @Bean
    public TeacherMapper teacherMapper() {
        return new TeacherMapperImpl();
    }
}