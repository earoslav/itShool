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
import java.util.*;

// MailService contains business operations for the "email-notifications" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    // Receives JavaMailSender and Thymeleaf TemplateEngine for HTML emails.
    // All public methods below only prepare variables, while the actual sending is performed by the internal send method.
    public MailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }
    // The single internal method that gathers Thymeleaf Context, renders HTML, and sends a MIME email to each recipient.
    // Before rendering, it formats lesson times in Ukrainian so that email templates show readable date, time, and duration.
    private void send(String template, Map<String, Object> variables) throws MessagingException {
        Mail mail = new Mail();
        mail.setTo((List<String>) variables.get("to"));
        mail.setSubject((String) variables.get("subject"));
        mail.setBody("");
        Context context = new Context();
        variables.keySet().stream().forEach(val->{
            if(val.equals("timeCourse")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, HH:mm", Locale.ENGLISH);
                LocalDateTime date = (LocalDateTime) variables.get("timeCourse");
                String formatedDate = date.format(formatter);
                context.setVariable("time", formatedDate);
            }
            if(val.equals("timeLesson")){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM HH:mm", Locale.ENGLISH);
                LocalDateTime date = (LocalDateTime) variables.get("timeLesson");
                String formatedDate = date.format(formatter);
                context.setVariable("time", formatedDate);
            }
            if(val.equals("timeLessonOld")){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM HH:mm", Locale.ENGLISH);
                LocalDateTime date = (LocalDateTime) variables.get("timeLessonOld");
                String formatedDate = date.format(formatter);
                context.setVariable("timeOld", formatedDate);
            }
            if(val.equals("timeLessonNew")){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM HH:mm", Locale.ENGLISH);
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
    // Prepares an email for the admin with new teacher data, password, courses, and free times for confirmation.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toAdmin/mailTemplateForAddNewTeacher", and delegates sending to the send method.
    public void sendEmailWithThymeleaf(TeacherDTO teacher, String password, List<TimeOfTheWeekDTO> freeTimes, List<CourseDTO> courses, HashMap<String, List<String>> compiledTimes, String timeIds, String courseIds, String subject, List<String> emails) throws MessagingException {
        Map<String, Object> map = new HashMap<>();

        map.put("teacher", teacher);
        map.put("approved", false);
        map.put("addTeacherUrl", "http://localhost:8081/admin/addTeacher");
        map.put("courses", courses);
        map.put("freeTimes", freeTimes);
        map.put("password", password);
        map.put("compiledTimes", compiledTimes);
        map.put("timesIds", timeIds);
        map.put("coursesIds", courseIds);
        map.put("to", emails);
        map.put("subject", subject);
        send( "emailTemplates/toAdmin/mailTemplateForAddNewTeacher", map);
    }

    // Prepares an email for the student about the deletion of an entire course by the teacher.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toStudent/forDelete/mailTemplateForDeleteCourseByTeacher", and delegates sending to the send method.
    public void sendEmailWithThymeleafToStudentAboutCourseRemoved( TeacherDTO teacher, LocalDateTime time, float duration, String subject, List<String> emails) throws MessagingException {
        send("emailTemplates/toStudent/forDelete/mailTemplateForDeleteCourseByTeacher", Map.of(
                "teacher", teacher,
                "timeCourse", time,
                "duration", duration,
                "to", emails,
                "subject", subject

        ));
    }
    // Prepares an email for the student about the cancellation of a single lesson by the teacher.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toStudent/forDelete/mailTemplateForDeleteLessonByTeacher", and delegates sending to the send method.
    public void sendEmailWithThymeleafToStudentAboutLessonRemoved(TeacherDTO teacher, LocalDateTime time, float duration, String subject, List<String> emails) throws MessagingException {
        send( "emailTemplates/toStudent/forDelete/mailTemplateForDeleteLessonByTeacher", Map.of(
                "teacher", teacher,
                "timeLesson", time,
                "duration", duration,
                "to", emails,
                "subject", subject
        ));
    }
    // Prepares an email for the student about a lesson added by the teacher.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toStudent/forAdd/mailTemplateForAddLessonByTeacher", and delegates sending to the send method.
    public void sendEmailWithThymeleafToStudentAboutLessonAdded(TeacherDTO teacher, LocalDateTime time, float duration, String subject, List<String> emails) throws MessagingException {
        send("emailTemplates/toStudent/forAdd/mailTemplateForAddLessonByTeacher", Map.of(
                "teacher", teacher,
                "timeLesson", time,
                "duration", duration,
                "to", emails,
                "subject", subject
        ));
    }

    // Prepares an email for the student about an added course and the first lesson.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toStudent/forAdd/mailTemplateForAddCourseByTeacher", and delegates sending to the send method.
    public void sendEmailWithThymeleafToStudentAboutCourseAdded( TeacherDTO teacher, LocalDateTime time, float duration, String subject, List<String> emails) throws MessagingException {
        send("emailTemplates/toStudent/forAdd/mailTemplateForAddCourseByTeacher", Map.of(
                "teacher", teacher,
                "timeCourse", time,
                "duration", duration,
                "to", emails,
                "subject", subject
        ));
    }
    // Prepares an email for the student indicating that the teacher approved the lesson deletion.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toStudent/forDelete/mailTemplateForExceptDeleteLesson", and delegates sending to the send method.
    public void sendEmailWithThymeleafToStudentAboutDeleteLessonExcepted( TeacherDTO teacher, LocalDateTime time, float duration, String subject, List<String> emails) throws MessagingException {
        send( "emailTemplates/toStudent/forDelete/mailTemplateForExceptDeleteLesson", Map.of(
                "teacher", teacher,
                "timeLesson", time,
                "duration", duration,
                "to", emails,
                "subject", subject
        ));
    }
    // Prepares an email for the student indicating that the teacher denied the lesson deletion.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toStudent/forDelete/mailTemplateForDenyedDeleteLesson", and delegates sending to the send method.
    public void sendEmailWithThymeleafToStudentAboutDeleteLessonDenyed(TeacherDTO teacher, LocalDateTime time, float duration, String subject, List<String> emails) throws MessagingException {
        send( "emailTemplates/toStudent/forDelete/mailTemplateForDenyedDeleteLesson", Map.of(
                "teacher", teacher,
                "timeLesson", time,
                "duration", duration,
                "to", emails,
                "subject", subject
        ));
    }

    // Prepares a request for the teacher to confirm a lesson deletion by the student.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toTeacher/forDelete/mailTemplateForRequestForDeleteLessonByStudent", and delegates sending to the send method.
    public void sendRequestWithThymeleafTeacherAboutRequestLessonRemoved( StudentDTO student, int idTeach, int idLes, LocalDateTime time, float duration, String subject, List<String> emails) throws MessagingException {
        send("emailTemplates/toTeacher/forDelete/mailTemplateForRequestForDeleteLessonByStudent", Map.of(
                "student", student,
                "idTeach", idTeach,
                "idLess", idLes,
                "timeLesson", time,
                "duration", duration,
                "to", emails,
                "subject", subject
        ));
    }
    // Prepares an email for the teacher about the cancellation of a single lesson by the student.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toTeacher/forDelete/mailTemplateForDeleteLessonByStudent", and delegates sending to the send method.
    public void sendEmailWithThymeleafTeacherAboutLessonRemoved( StudentDTO student, LocalDateTime time, float duration, String subject, List<String> emails) throws MessagingException {
        send( "emailTemplates/toTeacher/forDelete/mailTemplateForDeleteLessonByStudent", Map.of(
                "student", student,
                "timeLesson", time,
                "duration", duration,
                "to", emails,
                "subject", subject
        ));
    }
    // Prepares an email for the teacher about the deletion of a course by the student.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toTeacher/forDelete/mailTemplateForDeleteCourseByStudent", and delegates sending to the send method.
    public void sendEmailWithThymeleafTeacherAboutCourseRemoved(StudentDTO student, LocalDateTime time, float duration, String subject, List<String> emails) throws MessagingException {
        send("emailTemplates/toTeacher/forDelete/mailTemplateForDeleteCourseByStudent", Map.of(
                "student", student,
                "timeCourse", time,
                "duration", duration,
                "to", emails,
                "subject", subject
        ));
    }

    // Prepares an email for the teacher about a lesson reschedule by the student.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toTeacher/forEdit/mailTemplateForEditLessonTimeByStudent", and delegates sending to the send method.
    public void sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(StudentDTO student, LocalDateTime oldTime, LocalDateTime newTime, float duration, String subject, List<String> emails) throws MessagingException {
        send("emailTemplates/toTeacher/forEdit/mailTemplateForEditLessonTimeByStudent", Map.of(
                "student", student,
                "timeLessonOld", oldTime,
                "timeLessonNew", newTime,
                "duration", duration,
                "to", emails,
                "subject", subject
        ));
    }

    // Prepares an email for the student about a lesson reschedule by the teacher.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toStudent/forEdit/mailTemplateForEditLessonTimeByTeacher", and delegates sending to the send method.
    public void sendEmailWithThymeleafToStudentAboutLessonTimeEdited(TeacherDTO teacher, LocalDateTime oldTime, LocalDateTime newTime, float duration, String subject, List<String> emails) throws MessagingException {
        send( "emailTemplates/toStudent/forEdit/mailTemplateForEditLessonTimeByTeacher", Map.of(
                "teacher", teacher,
                "timeLessonOld", oldTime,
                "timeLessonNew", newTime,
                "duration", duration,
                "to", emails,
                "subject", subject
        ));
    }

    // Prepares an email for the student indicating they were removed from the teacher's student list.
    // Passes the required DTOs and lesson times to template variables, retrieves HTML from "emailTemplates/toStudent/forDelete/mailTemplateForStudentRemovedFromTeacherStudents", and delegates sending to the send method.
    public void sendEmailWithThymeleafToStudentAboutTeacherRemover( TeacherDTO teacher, String subject, List<String> emails) throws MessagingException {
        send( "emailTemplates/toStudent/forDelete/mailTemplateForStudentRemovedFromTeacherStudents", Map.of(
                "teacher", teacher,
                "to", emails,
                "subject", subject
        ));
    }

}
