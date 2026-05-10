//package com.example.demo.services.email;
//
//import com.example.demo.dto.entities.StudentDTO;
//import com.example.demo.dto.entities.TeacherDTO;
//import com.example.demo.dto.other.TimeOfTheWeekDTO;
//import com.example.demo.dto.programe.CourseDTO;
//import com.example.demo.dto.programe.LessonDTO;
//import com.example.demo.models.entities.Student;
//import com.example.demo.models.entities.Teacher;
//import com.example.demo.models.email.Mail;
//import com.example.demo.models.other.TimeOfTheWeek;
//import com.example.demo.models.programe.Course;
//import com.example.demo.models.programe.Lesson;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//
//import java.util.HashMap;
//import java.util.List;
//
//@Service
//public class MailService  {
//
//
//
//    private JavaMailSender javaMailSender;
//
//    private final TemplateEngine templateEngine;
//
//
//
//    public MailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
//        this.javaMailSender = javaMailSender;
//        this.templateEngine = templateEngine;
//
//    }
//    public void sendEmailWithThymeleaf(Mail mail, TeacherDTO teacher, String password, List<TimeOfTheWeekDTO> freeTimes, List<CourseDTO> courses, HashMap<String, List<String>> compiledTimes, String timeIds, String courseIds) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("teacher", teacher);
//            context.setVariable("approved", false);
//            context.setVariable("addTeacherUrl", "http://localhost:8081/admin/addTeacher");
//            context.setVariable("courses", courses);
//            context.setVariable("freeTimes", freeTimes);
//            context.setVariable("password", password);
//            context.setVariable("compiledTimes", compiledTimes);
//            context.setVariable("timesIds", timeIds);
//            context.setVariable("coursesIds", courseIds);
//
//            String html = templateEngine.process("emailTemplates/toAdmin/mailTemplateForAddNewTeacher", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//    public void sendEmailWithThymeleafToStudentAboutCourseRemoved(Mail mail, TeacherDTO teacher, String day, int hour) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("teacher", teacher);
//            context.setVariable("day", day);
//            context.setVariable("hour", hour);
//            String html = templateEngine.process("emailTemplates/toStudent/forDelete/mailTemplateForDeleteCourseByTeacher", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//    public void sendEmailWithThymeleafToStudentAboutLessonRemoved(Mail mail, TeacherDTO teacher, String day, int hour) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("teacher", teacher);
//            context.setVariable("day", day);
//            context.setVariable("hour", hour);
//            String html = templateEngine.process("emailTemplates/toStudent/forDelete/mailTemplateForDeleteLessonByTeacher", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//    public void sendEmailWithThymeleafToStudentAboutLessonAdded(Mail mail, TeacherDTO teacher, String day, int hour) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("teacher", teacher);
//            context.setVariable("day", day);
//            context.setVariable("hour", hour);
//            String html = templateEngine.process("emailTemplates/toStudent/forAdd/mailTemplateForAddLessonByTeacher", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//    public void sendEmailWithThymeleafToStudentAboutCourseAdded(Mail mail, TeacherDTO teacher, String day, int hour) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("teacher", teacher);
//            context.setVariable("day", day);
//            context.setVariable("hour", hour);
//            String html = templateEngine.process("emailTemplates/toStudent/forAdd/mailTemplateForAddCourseByTeacher", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//
//    public void sendEmailWithThymeleafToStudentAboutDeleteLessonExcepted(Mail mail, TeacherDTO teacher , String day, int hour) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("teacher", teacher);
//            context.setVariable("day", day);
//            context.setVariable("hour", hour);
//            String html = templateEngine.process("emailTemplates/toStudent/forDelete/mailTemplateForExceptDeleteLesson", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//
//    public void sendEmailWithThymeleafToStudentAboutDeleteLessonDenyed(Mail mail, TeacherDTO teacher , String day, int hour) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("teacher", teacher);
//            context.setVariable("day", day);
//            context.setVariable("hour", hour);
//            String html = templateEngine.process("emailTemplates/toStudent/forDelete/mailTemplateForDenyedDeleteLesson", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//
//    public void sendRequestWithThymeleafTeacherAboutRequestLessonRemoved(Mail mail, StudentDTO student,int idTeach, int idLes, String day, int hour) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("student", student);
//            context.setVariable("idLess", idLes);
//            context.setVariable("idTeach", idTeach);
//            context.setVariable("day", day);
//            context.setVariable("hour", hour);
//            String html = templateEngine.process("emailTemplates/toTeacher/forDelete/mailTemplateForRequestForDeleteLessonByStudent", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//    public void sendEmailWithThymeleafTeacherAboutLessonRemoved(Mail mail, StudentDTO student, String day, int hour) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("student", student);
//            context.setVariable("day", day);
//            context.setVariable("hour", hour);
//            String html = templateEngine.process("emailTemplates/toTeacher/forDelete/mailTemplateForDeleteLessonByStudent", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//    public void sendEmailWithThymeleafTeacherAboutCourseRemoved(Mail mail, StudentDTO student, String day, int hour) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("student", student);
//            context.setVariable("day", day);
//            context.setVariable("hour", hour);
//            String html = templateEngine.process("emailTemplates/toTeacher/forDelete/mailTemplateForDeleteCourseByStudent", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//    public void sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(Mail mail, StudentDTO student, String day, int hour, String newDay, int newHour) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("student", student);
//            context.setVariable("day", day);
//            context.setVariable("hour", hour);
//            context.setVariable("newDay", newDay);
//            context.setVariable("newHour", newHour);
//            String html = templateEngine.process("emailTemplates/toTeacher/forEdit/mailTemplateForEditLessonTimeByStudent", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//    public void sendEmailWithThymeleafToStudentAboutLessonTimeEdited(Mail mail, TeacherDTO teacher, String day, int hour, String newDay, int newHour) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("teacher", teacher);
//            context.setVariable("day", day);
//            context.setVariable("hour", hour);
//            context.setVariable("newDay", newDay);
//            context.setVariable("newHour", newHour);
//            String html = templateEngine.process("emailTemplates/toStudent/forEdit/mailTemplateForEditLessonTimeByTeacher", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//    public void sendEmailWithThymeleafToStudentAboutTeacherRemover(Mail mail, TeacherDTO teacher) throws MessagingException {
//        for (String recipient : mail.getTo()) {
//            Context context = new Context();
//            context.setVariable("teacher", teacher);
//            String html = templateEngine.process("emailTemplates/toStudent/forDelete/mailTemplateForStudentRemovedFromTeacherStudents", context);
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            helper.setSubject(mail.getSubject());
//            helper.setFrom("itkidsonlineitschool@gmail.com");
//            helper.setTo(recipient);
//            helper.setText(html, true);
//            javaMailSender.send(mimeMessage);
//        }
//    }
//
//
//
//}
package com.example.demo.services.email;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.dto.other.TimeOfTheWeekDTO;
import com.example.demo.dto.programe.CourseDTO;
import com.example.demo.models.email.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// Сервіс MailService містить бізнес-операції для модуля «email-повідомлення».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    // Отримує JavaMailSender і Thymeleaf TemplateEngine для HTML-листів.
    // Усі публічні методи нижче лише готують змінні, а фактичну відправку виконує внутрішній send.
    public MailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }
    // Єдиний внутрішній метод, який збирає Thymeleaf Context, рендерить HTML і надсилає MIME-лист кожному отримувачу.
    // Перед рендерингом він форматує часи уроків українською, щоб email-шаблони показували читабельні дату, час і тривалість.
    private void send(Mail mail, String template, Map<String, Object> variables) throws MessagingException {
        Context context = new Context();
        variables.keySet().stream().forEach(val->{
            if(val.equals("timeCourse")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, HH:mm", new Locale("uk", "UA"));
                LocalDateTime date = (LocalDateTime) variables.get("timeCourse");
                String formatedDate = date.format(formatter);
                context.setVariable("time", formatedDate);
            }
            if(val.equals("timeLesson")){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM HH:mm", new Locale("uk", "UA"));
                LocalDateTime date = (LocalDateTime) variables.get("timeLesson");
                String formatedDate = date.format(formatter);
                context.setVariable("time", formatedDate);
            }
            if(val.equals("timeLessonOld")){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM HH:mm", new Locale("uk", "UA"));
                LocalDateTime date = (LocalDateTime) variables.get("timeLessonOld");
                String formatedDate = date.format(formatter);
                context.setVariable("timeOld", formatedDate);
            }
            if(val.equals("timeLessonNew")){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM HH:mm", new Locale("uk", "UA"));
                LocalDateTime date = (LocalDateTime) variables.get("timeLessonNew");
                String formatedDate = date.format(formatter);
                context.setVariable("timeNew", formatedDate);
            }
            if(!val.equals("timeCourse") && !val.equals("timeLesson") && !val.equals("timeLessonOld") && !val.equals("timeLessonNew")){
                context.setVariable(val, variables.get(val));
            }
        });
        String html = templateEngine.process(template, context);

        for (String recipient : mail.getTo()) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    // Готує лист адміну з даними нового викладача, паролем, курсами та вільним часом для підтвердження.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toAdmin/mailTemplateForAddNewTeacher" і делегує відправку send.
    public void sendEmailWithThymeleaf(Mail mail, TeacherDTO teacher, String password, List<TimeOfTheWeekDTO> freeTimes, List<CourseDTO> courses, HashMap<String, List<String>> compiledTimes, String timeIds, String courseIds) throws MessagingException {
        send(mail, "emailTemplates/toAdmin/mailTemplateForAddNewTeacher", Map.of(
                "teacher", teacher,
                "approved", false,
                "addTeacherUrl", "http://localhost:8081/admin/addTeacher",
                "courses", courses,
                "freeTimes", freeTimes,
                "password", password,
                "compiledTimes", compiledTimes,
                "timesIds", timeIds,
                "coursesIds", courseIds
        ));
    }

    // Готує лист студенту про видалення всього курсу викладачем.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toStudent/forDelete/mailTemplateForDeleteCourseByTeacher" і делегує відправку send.
    public void sendEmailWithThymeleafToStudentAboutCourseRemoved(Mail mail, TeacherDTO teacher, LocalDateTime time, float duration) throws MessagingException {
        send(mail, "emailTemplates/toStudent/forDelete/mailTemplateForDeleteCourseByTeacher", Map.of(
                "teacher", teacher,
                "timeCourse", time,
                "duration", duration

        ));
    }
    // Готує лист студенту про скасування одного заняття викладачем.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toStudent/forDelete/mailTemplateForDeleteLessonByTeacher" і делегує відправку send.
    public void sendEmailWithThymeleafToStudentAboutLessonRemoved(Mail mail, TeacherDTO teacher, LocalDateTime time, float duration) throws MessagingException {
        send(mail, "emailTemplates/toStudent/forDelete/mailTemplateForDeleteLessonByTeacher", Map.of(
                "teacher", teacher,
                "timeLesson", time,
                "duration", duration
        ));
    }
    // Готує лист студенту про доданий викладачем урок.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toStudent/forAdd/mailTemplateForAddLessonByTeacher" і делегує відправку send.
    public void sendEmailWithThymeleafToStudentAboutLessonAdded(Mail mail, TeacherDTO teacher, LocalDateTime time, float duration) throws MessagingException {
        send(mail, "emailTemplates/toStudent/forAdd/mailTemplateForAddLessonByTeacher", Map.of(
                "teacher", teacher,
                "timeLesson", time,
                "duration", duration
        ));
    }

    // Готує лист студенту про доданий курс і перше заняття.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toStudent/forAdd/mailTemplateForAddCourseByTeacher" і делегує відправку send.
    public void sendEmailWithThymeleafToStudentAboutCourseAdded(Mail mail, TeacherDTO teacher, LocalDateTime time, float duration) throws MessagingException {
        send(mail, "emailTemplates/toStudent/forAdd/mailTemplateForAddCourseByTeacher", Map.of(
                "teacher", teacher,
                "timeCourse", time,
                "duration", duration
        ));
    }
    // Готує лист студенту, що викладач погодив видалення уроку.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toStudent/forDelete/mailTemplateForExceptDeleteLesson" і делегує відправку send.
    public void sendEmailWithThymeleafToStudentAboutDeleteLessonExcepted(Mail mail, TeacherDTO teacher, LocalDateTime time, float duration) throws MessagingException {
        send(mail, "emailTemplates/toStudent/forDelete/mailTemplateForExceptDeleteLesson", Map.of(
                "teacher", teacher,
                "timeLesson", time,
                "duration", duration
        ));
    }
    // Готує лист студенту, що викладач відхилив видалення уроку.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toStudent/forDelete/mailTemplateForDenyedDeleteLesson" і делегує відправку send.
    public void sendEmailWithThymeleafToStudentAboutDeleteLessonDenyed(Mail mail, TeacherDTO teacher, LocalDateTime time, float duration) throws MessagingException {
        send(mail, "emailTemplates/toStudent/forDelete/mailTemplateForDenyedDeleteLesson", Map.of(
                "teacher", teacher,
                "timeLesson", time,
                "duration", duration
        ));
    }

    // Готує запит викладачу на підтвердження видалення уроку студентом.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toTeacher/forDelete/mailTemplateForRequestForDeleteLessonByStudent" і делегує відправку send.
    public void sendRequestWithThymeleafTeacherAboutRequestLessonRemoved(Mail mail, StudentDTO student, int idTeach, int idLes, LocalDateTime time, float duration) throws MessagingException {
        send(mail, "emailTemplates/toTeacher/forDelete/mailTemplateForRequestForDeleteLessonByStudent", Map.of(
                "student", student,
                "idTeach", idTeach,
                "idLess", idLes,
                "timeLesson", time,
                "duration", duration
        ));
    }
    // Готує лист викладачу про скасування одного заняття студентом.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toTeacher/forDelete/mailTemplateForDeleteLessonByStudent" і делегує відправку send.
    public void sendEmailWithThymeleafTeacherAboutLessonRemoved(Mail mail, StudentDTO student, LocalDateTime time, float duration) throws MessagingException {
        send(mail, "emailTemplates/toTeacher/forDelete/mailTemplateForDeleteLessonByStudent", Map.of(
                "student", student,
                "timeLesson", time,
                "duration", duration
        ));
    }
    // Готує лист викладачу про видалення курсу студентом.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toTeacher/forDelete/mailTemplateForDeleteCourseByStudent" і делегує відправку send.
    public void sendEmailWithThymeleafTeacherAboutCourseRemoved(Mail mail, StudentDTO student, LocalDateTime time, float duration) throws MessagingException {
        send(mail, "emailTemplates/toTeacher/forDelete/mailTemplateForDeleteCourseByStudent", Map.of(
                "student", student,
                "timeCourse", time,
                "duration", duration
        ));
    }

    // Готує лист викладачу про перенесення уроку студентом.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toTeacher/forEdit/mailTemplateForEditLessonTimeByStudent" і делегує відправку send.
    public void sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(Mail mail, StudentDTO student, LocalDateTime oldTime, LocalDateTime newTime, float duration) throws MessagingException {
        send(mail, "emailTemplates/toTeacher/forEdit/mailTemplateForEditLessonTimeByStudent", Map.of(
                "student", student,
                "timeLessonOld", oldTime,
                "timeLessonNew", newTime,
                "duration", duration
        ));
    }

    // Готує лист студенту про перенесення уроку викладачем.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toStudent/forEdit/mailTemplateForEditLessonTimeByTeacher" і делегує відправку send.
    public void sendEmailWithThymeleafToStudentAboutLessonTimeEdited(Mail mail, TeacherDTO teacher, LocalDateTime oldTime, LocalDateTime newTime, float duration) throws MessagingException {
        send(mail, "emailTemplates/toStudent/forEdit/mailTemplateForEditLessonTimeByTeacher", Map.of(
                "teacher", teacher,
                "timeLessonOld", oldTime,
                "timeLessonNew", newTime,
                "duration", duration
        ));
    }

    // Готує лист студенту, що його прибрали зі списку учнів викладача.
    // У змінні шаблону передає потрібні DTO і час уроку, а HTML бере з "emailTemplates/toStudent/forDelete/mailTemplateForStudentRemovedFromTeacherStudents" і делегує відправку send.
    public void sendEmailWithThymeleafToStudentAboutTeacherRemover(Mail mail, TeacherDTO teacher) throws MessagingException {
        send(mail, "emailTemplates/toStudent/forDelete/mailTemplateForStudentRemovedFromTeacherStudents", Map.of(
                "teacher", teacher
        ));
    }

}
