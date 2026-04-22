package com.example.demo.services.email;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.dto.programe.LessonDTO;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.email.Mail;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;

@Service
public class MailService  {



    private JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;



    public MailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        ;

        this.templateEngine = templateEngine;

    }
    public void sendEmailWithThymeleaf(Mail mail, Teacher teacher, List<TimeOfTheWeek> freeTimes, List<Course> courses, HashMap<String, List<String>> compiledTimes, List<Integer> timeIds, List<Integer> courseIds) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("approved", false);
            context.setVariable("addTeacherUrl", "http://localhost:8081/admin/addTeacher");
            context.setVariable("courses", courses);
            context.setVariable("freeTimes", freeTimes);
            context.setVariable("compiledTimes", compiledTimes);
            context.setVariable("timesIds", timeIds);
            context.setVariable("coursesIds", courseIds);

            String html = templateEngine.process("emailTemplates/toAdmin/mailTemplateForAddNewTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutCourseRemoved(Mail mail, TeacherDTO teacher, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/toStudent/forDelete/mailTemplateForDeleteCourseByTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutLessonRemoved(Mail mail, TeacherDTO teacher, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/toStudent/forDelete/mailTemplateForDeleteLessonByTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutLessonAdded(Mail mail, TeacherDTO teacher, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/toStudent/forAdd/mailTemplateForAddLessonByTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutCourseAdded(Mail mail, TeacherDTO teacher, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/toStudent/forAdd/mailTemplateForAddCourseByTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }

    public void sendEmailWithThymeleafToStudentAboutDeleteLessonExcepted(Mail mail, TeacherDTO teacher , String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/toStudent/forDelete/mailTemplateForExceptDeleteLesson", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }

    public void sendEmailWithThymeleafToStudentAboutDeleteLessonDenyed(Mail mail, TeacherDTO teacher , String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/toStudent/forDelete/mailTemplateForDenyedDeleteLesson", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }

    public void sendRequestWithThymeleafTeacherAboutRequestLessonRemoved(Mail mail, StudentDTO student,int idTeach, int idLes, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("student", student);
            context.setVariable("idLess", idLes);
            context.setVariable("idTeach", idTeach);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/toTeacher/forDelete/mailTemplateForRequestForDeleteLessonByStudent", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafTeacherAboutLessonRemoved(Mail mail, StudentDTO student, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("student", student);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/toTeacher/forDelete/mailTemplateForDeleteLessonByStudent", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafTeacherAboutCourseRemoved(Mail mail, StudentDTO student, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("student", student);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/toTeacher/forDelete/mailTemplateForDeleteCourseByStudent", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(Mail mail, StudentDTO student, String day, int hour, String newDay, int newHour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("student", student);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            context.setVariable("newDay", newDay);
            context.setVariable("newHour", newHour);
            String html = templateEngine.process("emailTemplates/toTeacher/forEdit/mailTemplateForEditLessonTimeByStudent", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutLessonTimeEdited(Mail mail, TeacherDTO teacher, String day, int hour, String newDay, int newHour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            context.setVariable("newDay", newDay);
            context.setVariable("newHour", newHour);
            String html = templateEngine.process("emailTemplates/toStudent/forEdit/mailTemplateForEditLessonTimeByTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutTeacherRemover(Mail mail, TeacherDTO teacher) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            String html = templateEngine.process("emailTemplates/toStudent/forDelete/mailTemplateForStudentRemovedFromTeacherStudents", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }



}
