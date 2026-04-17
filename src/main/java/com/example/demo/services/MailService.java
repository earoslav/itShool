package com.example.demo.services;

import com.example.demo.models.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.InputStream;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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

            String html = templateEngine.process("emailTemplates/mailTemplateForAddNewTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutCourseRemoved(Mail mail, Teacher teacher, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/mailTemplateForDeleteCourseByTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutLessonRemoved(Mail mail, Teacher teacher, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/mailTemplateForDeleteLessonByTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutLessonAdded(Mail mail, Teacher teacher, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/mailTemplateForAddLessonByTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutCourseAdded(Mail mail, Teacher teacher, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/mailTemplateForAddCourseByTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }

    public void sendEmailWithThymeleafTeacherAboutLessonRemoved(Mail mail, Student student, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("student", student);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/mailTemplateForDeleteLessonByStudent", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafTeacherAboutCourseRemoved(Mail mail, Student student, String day, int hour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("student", student);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            String html = templateEngine.process("emailTemplates/mailTemplateForDeleteCourseByStudent", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(Mail mail, Student student, String day, int hour, String newDay, int newHour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("student", student);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            context.setVariable("newDay", newDay);
            context.setVariable("newHour", newHour);
            String html = templateEngine.process("emailTemplates/mailTemplateForEditLessonTimeByStudent", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutLessonTimeEdited(Mail mail, Teacher teacher, String day, int hour, String newDay, int newHour) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);
            context.setVariable("hour", hour);
            context.setVariable("newDay", newDay);
            context.setVariable("newHour", newHour);
            String html = templateEngine.process("emailTemplates/mailTemplateForEditLessonTimeByTeacher", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setFrom("itkidsonlineitschool@gmail.com");
            helper.setTo(recipient);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        }
    }
    public void sendEmailWithThymeleafToStudentAboutTeacherRemover(Mail mail, Teacher teacher) throws MessagingException {
        for (String recipient : mail.getTo()) {
            Context context = new Context();
            context.setVariable("teacher", teacher);
            String html = templateEngine.process("emailTemplates/mailTemplateForStudentRemovedFromTeacherStudents", context);
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
