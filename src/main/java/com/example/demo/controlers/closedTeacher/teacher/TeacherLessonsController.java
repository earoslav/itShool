package com.example.demo.controlers.closedTeacher.teacher;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.example.demo.services.email.MailService;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.TeacherCourseService;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class TeacherLessonsController {
    private TeacherService teacherService;
    private LessonMapper lessonMapper;
    private TimeOfTheWeekService theWeekService;
    private CourseService courseService;
    private EmptyTimesForTeacherService emptyTimesForTeacherService;
    private LessonService lessonService;
    private TeacherCourseService teacherCourseService;
    private MailService mailService;
    private TeacherStudentTimeOfTheWeekService tswService;
    private StudentService studentService;
    private StudentMapper studentMapper;
    private TeacherMapper teacherMapper;
    private CourseMapper courseMapper;

    public TeacherLessonsController(TeacherService teacherService, LessonMapper lessonMapper, TimeOfTheWeekService theWeekService, CourseService courseService, EmptyTimesForTeacherService emptyTimesForTeacherService, LessonService lessonService, TeacherCourseService teacherCourseService, MailService mailService, TeacherStudentTimeOfTheWeekService tswService, StudentService studentService, StudentMapper studentMapper, TeacherMapper teacherMapper, CourseMapper courseMapper) {
        this.teacherService = teacherService;
        this.lessonMapper = lessonMapper;
        this.theWeekService = theWeekService;
        this.courseService = courseService;
        this.emptyTimesForTeacherService = emptyTimesForTeacherService;
        this.lessonService = lessonService;
        this.teacherCourseService = teacherCourseService;
        this.mailService = mailService;
        this.tswService = tswService;
        this.studentService = studentService;
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
        this.courseMapper = courseMapper;
    }

    @GetMapping("/{id}/lessons")
    public String gotoLessons(@PathVariable("id") int id, Model model) {
        Teacher teacher = teacherService.getById(id);
        List<Object> values = lessonService.compileLessonsForTeacher(teacher.getId());
        List<String> weekDays = (List<String>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(2);

        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", studentService.getAll().stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedTeacher/teacherLessons/lessons";
    }

    @PostMapping("/{idTeach}/lessons/editLesson/notPicked")
    public String editLessoneFromTeacherWithNotPicked(@PathVariable("idTeach") int idTeach) {
        return "redirect:/teacher/" + idTeach + "/lessons/itemNotPicked";
    }

    @GetMapping("/{id}/lessons/{output}")
    public String gotoLessonsWithOutput(@PathVariable("id") int id, @PathVariable("output") String output, Model model) {
        Teacher teacher = teacherService.getById(id);

        List<Object> values = lessonService.compileLessonsForTeacher(teacher.getId());
        List<String> weekDays = (List<String>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(2);
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));
        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("output", output);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", studentService.getAll().stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedTeacher/teacherLessons/lessons";
    }

    @PostMapping("/{idTeach}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lessonService.getById(idLes).getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lessonService.getById(idLes).getLessonTime(), lessonService.getById(idLes).getDuration());
        lessonService.deleteById(idLes);
        return "redirect:/teacher/" + idTeach + "/lessons/lessonDeleted";
    }

    @PostMapping("/{idTeach}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourse(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), lesson.getDuration());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId());
        return "redirect:/teacher/" + idTeach + "/lessons/courseDeleted";
    }

    @PostMapping("/{idTeach}/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("nTId") int newTimeId, @PathVariable("date") String date, Model model) throws MessagingException {
        TimeOfTheWeek newTime = theWeekService.getById(newTimeId);
        Lesson lesson = lessonService.getById(idLes);
        float dur = lesson.getDuration();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newDate = LocalDateTime.now().withYear(Integer.parseInt(date.split("\\.")[2].split("-")[0])).withMonth(Integer.parseInt(date.split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split("\\.")[0])).withHour(newTime.getTimeOfTheDay()).withMinute(Integer.parseInt(date.split("-")[1])).withSecond(0);
        LocalDateTime time = newDate;
        if (newDate.getDayOfWeek().getValue() == newTime.getDayOfTheWeek()) {
            LocalDateTime lessonFinish = time;
            if (dur == (long) dur) {
                lessonFinish = lessonFinish.plusHours((long) dur);
            } else {
                lessonFinish = lessonFinish.plusHours((long) dur).plusMinutes(30);
            }
            if (lessonFinish.isBefore(time.withHour(22).withMinute(1))) {
                AtomicBoolean overlap = new AtomicBoolean(false);
                List<Lesson> teacherLessons = lessonService.findAllByTeachId(idTeach);
                LocalDateTime finalLessonFinish = lessonFinish;
                LocalDateTime finalTime = time;
                teacherLessons.stream().filter(les->les.getId()!=lesson.getId()).forEach(l -> {
                    LocalDateTime lFinish = l.getLessonTime();
                    float lDur = l.getDuration();
                    if (lDur == (long) lDur) {
                        lFinish = lFinish.plusHours((long) lDur);
                    } else {
                        lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
                    }
                    if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime()))) {
                        overlap.set(true);
                    }
                });


                List<Lesson> studentLessons = lessonService.findAllByStudentId(lesson.getStudent().getId());
                studentLessons.stream().filter(les->les.getId()!=lesson.getId()).forEach(l -> {
                    LocalDateTime lFinish = l.getLessonTime();
                    float lDur = l.getDuration();
                    if (lDur == (long) lDur) {
                        lFinish = lFinish.plusHours((long) lDur);
                    } else {
                        lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
                    }
                    if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime()))) {
                        overlap.set(true);
                    }
                });
                if (!overlap.get()) {
                    Mail mail = new Mail();
                    mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
                    mail.setSubject("Лист про зміну часу одного заняття");
                    mail.setBody("");
                    Teacher teacher = teacherService.getById(idTeach);
                    mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), newDate, dur);
                    lesson.setLessonTime(newDate);
                    lessonService.update(lesson.getId(), lesson);
                    return "redirect:/teacher/" + idTeach + "/lessons/lessonEdited";
                } else {
                    return "redirect:/teacher/" + idTeach + "/lessons/lessonOverlap";
                }
            } else {
                return "redirect:/teacher/" + idTeach + "/lessons/lessonAfterAcceptedTime";
            }
        } else {
            return "redirect:/teacher/" + idTeach + "/lessons/dateInconsistency";
        }

    }

    @PostMapping("/{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}/{dur}")
    public String addLesson(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        int dayOfMonth = Integer.parseInt(date.split(" ")[1].split("\\.")[0]);
        int month = Integer.parseInt(date.split(" ")[1].split("\\.")[1]);
        int hour = Integer.parseInt(date.split(" ")[2].split(":")[0]);
        int minute = Integer.parseInt(date.split(" ")[2].split(":")[1]);
        LocalDateTime time = LocalDateTime.now();
        if (time.getMonth().getValue() == 12 && month == 1) {
            time = time.withYear(time.getYear() + 1).withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(minute).withSecond(0);
        } else {
            time = time.withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(minute).withSecond(0);
        }
        if (time.isAfter(LocalDateTime.now())) {
            if (cId != 0) {
                if (stId != 0) {
                    LocalDateTime lessonFinish = time;
                    if (dur == (long) dur) {
                        lessonFinish = lessonFinish.plusHours((long) dur);
                    } else {
                        lessonFinish = lessonFinish.plusHours((long) dur).plusMinutes(30);
                    }
                    if (lessonFinish.isBefore(time.withHour(22).withMinute(1))) {
                        AtomicBoolean overlap = new AtomicBoolean(false);
                        List<Lesson> teacherLessons = lessonService.findAllByTeachId(idTeach);
                        LocalDateTime finalLessonFinish = lessonFinish;
                        LocalDateTime finalTime = time;
                        teacherLessons.stream().forEach(l -> {
                            LocalDateTime lFinish = l.getLessonTime();
                            float lDur = l.getDuration();
                            if (lDur == (long) lDur) {
                                lFinish = lFinish.plusHours((long) lDur);
                            } else {
                                lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
                            }
                            if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime()))) {
                                overlap.set(true);
                            }

                        });
                        List<Lesson> studentLessons = lessonService.findAllByStudentId(stId);
                        studentLessons.stream().forEach(l -> {
                            LocalDateTime lFinish = l.getLessonTime();
                            float lDur = l.getDuration();
                            if (lDur == (long) lDur) {
                                lFinish = lFinish.plusHours((long) lDur);
                            } else {
                                lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
                            }
                            if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime()))) {
                                overlap.set(true);
                            }
                        });
                        if (!overlap.get()) {
                            Lesson lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur, theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()), "will");
                            lessonService.create(lesson);
                            Mail mail = new Mail();
                            mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
                            mail.setSubject("Лист про додання одного заняття");
                            mail.setBody("");
                            Teacher teacher = teacherService.getById(idTeach);
                            mailService.sendEmailWithThymeleafToStudentAboutLessonAdded(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), dur);
                            return "redirect:/teacher/" + idTeach + "/lessons/lessonAdded";
                        } else {
                            return "redirect:/teacher/" + idTeach + "/lessons/lessonOverlap";
                        }


                    } else {
                        return "redirect:/teacher/" + idTeach + "/lessons/lessonAfterAcceptedTime";
                    }
                } else {
                    return "redirect:/teacher/" + idTeach + "/lessons/studentNotFound";
                }
            } else {
                return "redirect:/teacher/" + idTeach + "/lessons/courseNotFound";
            }

        } else {
            return "redirect:/teacher/" + idTeach + "/lessons/lessonBeforeNow";
        }
    }

    @PostMapping("/{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}/{dur}")
    public String addCourse(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        int dayOfMonth = Integer.parseInt(date.split(" ")[1].split("\\.")[0]);
        int month = Integer.parseInt(date.split(" ")[1].split("\\.")[1]);
        int hour = Integer.parseInt(date.split(" ")[2].split(":")[0]);
        int minut = Integer.parseInt(date.split(" ")[2].split(":")[1]);
        LocalDateTime time = LocalDateTime.now();
        if (time.getMonth().getValue() == 12 && month == 1) {
            time = time.withYear(time.getYear() + 1).withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(minut).withSecond(0).withNano(0);
        } else {
            time = time.withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(minut).withSecond(0).withNano(0);
        }
        boolean hasCourse = false;
        for (TeacherStudentTimeOfTheWeek tsw : tswService.findAllByTeachId(idTeach)) {
            if (tsw.getTimeOfTheWeek().getDayOfTheWeek() == time.getDayOfWeek().getValue() && tsw.getTimeOfTheWeek().getTimeOfTheDay() == time.getHour()) {
                hasCourse = true;
            }
        }
        LocalDateTime lessonFinish = time;
        if (dur == (long) dur) {
            lessonFinish = lessonFinish.plusHours((long) dur);
        } else {
            lessonFinish = lessonFinish.plusHours((long) dur).plusMinutes(30);
        }
        if (lessonFinish.isAfter(time.withHour(22).withMinute(1))) {
            return "redirect:/teacher/" + idTeach + "/lessons/lessonAfterAcceptedTime";
        }
        if (time.isAfter(LocalDateTime.now())) {
            if (cId != 0) {
                if (stId != 0) {
                    AtomicBoolean exists = new AtomicBoolean(false);
                    Lesson lesson = new Lesson();
                    LocalDateTime checkingTime = time;
                    AtomicBoolean overlap = new AtomicBoolean(false);

                    for (int i = 1; i <= 5; i++) {
                        LocalDateTime finalLessonFinish = lessonFinish;
                        LocalDateTime finalTime = checkingTime;
                        for (Lesson les : teacherService.getById(idTeach).getLessons()) {

                            if (les.getLessonTime().truncatedTo(ChronoUnit.MINUTES).isEqual(checkingTime.truncatedTo(ChronoUnit.MINUTES))) {
                                exists.set(true);
                            } else {
                                LocalDateTime lFinish = les.getLessonTime();
                                float lDur = les.getDuration();
                                if (lDur == (long) lDur) {
                                    lFinish = lFinish.plusHours((long) lDur);
                                } else {
                                    lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
                                }
                                if ((les.getLessonTime().isAfter(finalTime) && les.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(les.getLessonTime()))) {
                                    overlap.set(true);
                                }
                            }

                        }
                        List<Lesson> studentLessons = lessonService.findAllByStudentId(stId);
                        studentLessons.stream().forEach(l -> {
                            LocalDateTime lFinish = l.getLessonTime();
                            float lDur = l.getDuration();
                            if (lDur == (long) lDur) {
                                lFinish = lFinish.plusHours((long) lDur);
                            } else {
                                lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
                            }
                            if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime()))) {
                                overlap.set(true);
                            }
                        });
                        checkingTime = checkingTime.plusWeeks(1);
                        lessonFinish = lessonFinish.plusWeeks(1);
                    }
                    if (overlap.get()) {
                        return "redirect:/teacher/" + idTeach + "/lessons/lessonOverlap";
                    }
                    if (exists.get() || hasCourse) {
                        return "redirect:/teacher/" + idTeach + "/lessons/timeNotEmpty";
                    } else {
                        for (int i = 1; i <= 4; i++) {
                            lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur, theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()), "will");
                            lessonService.create(lesson);

                            time = time.plusWeeks(1);
                        }
                        TeacherStudentTimeOfTheWeek tsw = new TeacherStudentTimeOfTheWeek(teacherService.getById(idTeach), studentService.getById(stId), theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()), courseService.getById(cId));
                        tswService.create(tsw);

                        Mail mail = new Mail();
                        mail.setTo(Collections.singletonList(studentService.getById(stId).getEmail()));
                        mail.setSubject("Лист про додання курсу занять");
                        mail.setBody("");
                        Teacher teacher = teacherService.getById(idTeach);
                        mailService.sendEmailWithThymeleafToStudentAboutCourseAdded(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), time, dur);
                        return "redirect:/teacher/" + idTeach + "/lessons/courseAdded";
                    }
                } else {
                    return "redirect:/teacher/" + idTeach + "/lessons/studentNotFound";
                }
            } else {
                return "redirect:/teacher/" + idTeach + "/lessons/courseNotFound";
            }

        } else {
            return "redirect:/teacher/" + idTeach + "/lessons/lessonBeforeNow";
        }
    }

    @PostMapping("/{idTeach}/addMoneyByLesson/{lesId}")
    public String addMoney(@PathVariable("idTeach") int teachId, @PathVariable("lesId") int lesId, Model model) {
        Teacher teacher = teacherService.getById(teachId);
        Lesson lesson = lessonService.getById(lesId);
        if (LocalDateTime.now().isAfter(lesson.getLessonTime())) {
            float sum = lesson.getDuration() * lesson.getCourse().getTeacherShare();
            teacher.setUnpaidMoney(teacher.getUnpaidMoney() + sum);
            teacherService.update(teachId, teacher);
            lesson.setStatus("was");
            lessonService.update(lesId, lesson);
            return "redirect:/teacher/" + teachId + "/lessons/lessonStatusChanged";
        } else {
            return "redirect:/teacher/" + teachId + "/lessons/tooEarly";
        }
    }

}
