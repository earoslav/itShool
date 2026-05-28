package com.example.demo.configs;

import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.entities.User;
import com.example.demo.repositories.entities.StudentRepository;
import com.example.demo.repositories.entities.TeacherRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
// Handler спрацьовує після успішного логіну і вирішує, куди перенаправити користувача.
// Він знаходить id студента або викладача через репозиторії, бо URL кабінетів у цьому проекті містять ці id.
@Component
@RequiredArgsConstructor
public class MyCustomSuccessHandler implements AuthenticationSuccessHandler {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    // Передає стандартну обробку успішної авторизації батьківському AuthenticationSuccessHandler.
    // Цей overload лишає сумісність з API, але реальне перенаправлення виконує версія без FilterChain нижче.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }
    // Після логіну визначає роль користувача і готує правильний URL кабінету.
    // Для ROLE_TEACHER і ROLE_STUDENT він через репозиторії знаходить id профілю, а ROLE_ADMIN веде напряму на /admin/homepage.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
        User user = (User) auth.getPrincipal();
        int userId = user.getId();
        int stId = 0;
        int teachId = 0;
        Student student = studentRepository.findByUserId(userId);
        if(student != null){
            stId = student.getId();
        }
        Teacher teacher = teacherRepository.findByUserId(userId);
        if(teacher != null){
            teachId = teacher.getId();
        }

        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/homepage");
        } else if(roles.contains("ROLE_TEACHER")) {
            response.sendRedirect("/teacher/"+teachId+"/homepage");
        }else if(roles.contains("ROLE_STUDENT")){
            response.sendRedirect("/student/"+stId+"/homepage");
        }else {
            response.sendRedirect("/openSource/homepage");
        }
    }

}
