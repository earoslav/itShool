package com.example.demo.services.program;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.dto.adminLessons.TeacherAdminLessonsDTO;
import com.example.demo.dto.adminLessons.TimeOfTheWeekAdminLessonsDTO;
import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.programe.LessonDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import com.example.demo.repositories.program.LessonRepository;
import com.example.demo.services.email.MailService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.example.demo.services.Statuses;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.TimeOfTheWeekService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// LessonService contains business operations for the "lessons" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
@Transactional
public class LessonService {
    private final LessonRepository lessonRepository;
    private LessonMapper lessonMapper;
    @Autowired
    @Lazy
    private TeacherService teacherService;
    private ObjectMapper objectMapper;

    private CourseService courseService;
    private StudentService studentService;
    private TimeOfTheWeekService theWeekService;
    private StudentMapper studentMapper;
    private TeacherMapper teacherMapper;
    private MailService mailService;
    private TeacherStudentTimeOfTheWeekService tswService;

    // Receives dependencies through Spring: LessonRepository, LessonMapper, TeacherService, ObjectMapper, TimeOfTheWeekService, CourseService, and StudentService.
    // These services and mappers are required by the class methods to work with the "lessons" module without manual object creation.
    public LessonService(LessonRepository lessonRepository, LessonMapper lessonMapper, TeacherService teacherService, ObjectMapper objectMapper, CourseService courseService, StudentService studentService, TimeOfTheWeekService theWeekService, StudentMapper studentMapper, TeacherMapper teacherMapper, MailService mailService, TeacherStudentTimeOfTheWeekService tswService) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.teacherService = teacherService;
        this.objectMapper = objectMapper;

        this.courseService = courseService;

        this.studentService = studentService;
        this.theWeekService = theWeekService;
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
        this.mailService = mailService;
        this.tswService = tswService;
    }
    // Returns all lessons from the repository.
    // Lists, calendars, and forms use this method when the entire set of available records needs to be shown.
    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }
    // Finds a single record in the "lessons" module by ID.
    public Lesson getById(int id) {
        Lesson obj = lessonRepository.findById(id);
        if (obj == null) throw new RuntimeException("Lesson not found with id: " + id);
        return obj;
    }
    public void manageDeleteLessonByStudentInAdmin(int idSt, int idLes) throws MessagingException {
        StudentDTO student = studentMapper.mapStudentToStudentDTO(studentService.getById(idSt));
        mailService.sendEmailWithThymeleafTeacherAboutLessonRemoved(student, getById(idLes).getLessonTime(), getById(idLes).getDuration(),"Lesson cancellation notification",Collections.singletonList((getById(idLes).getTeacher().getEmail())));
        deleteById(idLes);
    }
    
    public void manageDeleteLessonByTeacher(int idTeach, int idLes) throws MessagingException {
        TeacherDTO teacher = teacherMapper.mapTeacherToTeacherDTO(teacherService.getById(idTeach));
        mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved(teacher, getById(idLes).getLessonTime(), getById(idLes).getDuration(), "Lesson cancellation notification", Collections.singletonList(getById(idLes).getStudent().getEmail()));
        deleteById(idLes);
    }
    
    public void manageRequestDeleteLessonByStudent(int idSt, int idLes) throws MessagingException {
        StudentDTO student = studentMapper.mapStudentToStudentDTO(studentService.getById(idSt));
        mailService.sendRequestWithThymeleafTeacherAboutRequestLessonRemoved(student, getById(idLes).getTeacher().getId(), idLes, getById(idLes).getLessonTime(), getById(idLes).getDuration(), "Lesson cancellation notification", Collections.singletonList(getById(idLes).getTeacher().getEmail()));
    }
    public Map<String, Integer> calculateTeachersEarnings(){
        List<Teacher> teachers = teacherService.getAll();
        Map<String, Integer> teacherEarnings = new HashMap<>();
        for (Teacher t : teachers) {
            int total = 0;
            List<Lesson> lessons = findAllByTeachId(t.getId());
            for (Lesson l : lessons) {
                if ("WAS".equals(l.getStatus())) {
                    total += (int) (l.getDuration() * l.getCourse().getTeacherShare());
                }
            }
            teacherEarnings.put(t.getUser().getName(), total);
        }
        return teacherEarnings;
    }
    public void manageDeleteCourseByTeacher(int idTeach, int idLes) throws MessagingException {
        Lesson lesson = getById(idLes);
        TeacherDTO teacher = teacherMapper.mapTeacherToTeacherDTO(teacherService.getById(idTeach));
        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(teacher, lesson.getLessonTime(), lesson.getDuration(), "Course cancellation notification", Collections.singletonList(lesson.getStudent().getEmail()));
        deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek(), java.time.LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek());
    }

    public void manageDeleteCourseByStudent(int idSt, int idLes) throws MessagingException {
        Lesson lesson = getById(idLes);
        StudentDTO student = studentMapper.mapStudentToStudentDTO(studentService.getById(idSt));
        mailService.sendEmailWithThymeleafTeacherAboutCourseRemoved(student, lesson.getLessonTime(), lesson.getDuration(), "Course cancellation notification", Collections.singletonList(lesson.getTeacher().getEmail()));
        deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(idSt, lesson.getTeacher().getId(), lesson.getTimeOfTheWeek(), java.time.LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(idSt, lesson.getTeacher().getId(), lesson.getTimeOfTheWeek());
    }

    public void manageAcceptLessonDeleteByTeacher(int idTeach, int idLes) throws MessagingException {
        Lesson lesson = getById(idLes);
        TeacherDTO teacher = teacherMapper.mapTeacherToTeacherDTO(teacherService.getById(idTeach));
        mailService.sendEmailWithThymeleafToStudentAboutDeleteLessonExcepted(teacher, lesson.getLessonTime(), lesson.getDuration(), "Lesson cancellation accepted", Collections.singletonList(lesson.getStudent().getEmail()));
        deleteById(idLes);
    }

    public void manageDenyLessonDeleteByTeacher(int idTeach, int idLes) throws MessagingException {
        Lesson lesson = getById(idLes);
        TeacherDTO teacher = teacherMapper.mapTeacherToTeacherDTO(teacherService.getById(idTeach));
        mailService.sendEmailWithThymeleafToStudentAboutDeleteLessonDenyed(teacher, lesson.getLessonTime(), lesson.getDuration(), "Lesson cancellation denied", Collections.singletonList(lesson.getStudent().getEmail()));
    }
    // Deletes all lessons for a specific student.
    // This method is used when cleaning up student data to ensure no orphaned lessons remain in the schedule.
    public void deleteAllLessonsByStudent(Student student) {
        lessonRepository.deleteAllBy(student);
    }
    // Saves a new record in the "lessons" module.
    // This method is called after the controller has collected the entity from a form or a service has generated it automatically.
    public Lesson create(Lesson lesson) {
        return lessonRepository.save(lesson);
    }
    // Returns a teacher's lessons associated with a specific weekly slot.
    // This is needed when changing available time to find lessons that depend on that slot.
    public List<Lesson> findAllByTimeOfTheWeekIdAndTeacherId(int timeOfWeekId, int teachId) {
        return lessonRepository.findAllByTimeOfTheWeekAndTeacherId(timeOfWeekId, teachId);
    }
    // Returns all lessons for a specific student.
    // This method is used by the student calendar and for overlap checks before rescheduling or creating a lesson.
    public List<Lesson> findAllByStudentId(int id) {
        return lessonRepository.findByStudentId(id);
    }

    public List<Lesson> findAllByTeacherIdAndLessonTimeAfterAndLessonTimeBefore(int teachId, LocalDateTime after, LocalDateTime before){
        return lessonRepository.findAllByTeacherIdAndLessonTimeAfterAndLessonTimeBefore(teachId, after, before);
    }

    // Checks if there is already a lesson in the database at an exact LocalDateTime.
    // Returns a boolean for quick duplicate checks in the schedule.
//    public boolean checkIfExistsByLessonTime(LocalDateTime time) {
//        return lessonRepository.findByLessonTime(time) != null;
//    }

    // Updates an existing record in the "lessons" module.
    // First finds the current entity, transfers student, teacher, course, lessonTime, and status fields, then saves it back to the repository.
    public Lesson update(Integer id, Lesson lesson) {
        Lesson existing = lessonRepository.findById((int)id);
        if (existing == null) throw new RuntimeException("Lesson not found");

        existing.setStudent(lesson.getStudent());
        existing.setTeacher(lesson.getTeacher());
        existing.setCourse(lesson.getCourse());
        existing.setLessonTime(lesson.getLessonTime());
        existing.setStatus(lesson.getStatus());
        
        lessonRepository.save(existing);
        
        // Fetch fully initialized entity (with student, teacher, course) for MailService
        Lesson updated = lessonRepository.findById((int)id);

        return updated;
    }

    // Checks if a teacher has a lesson at a specific time.
    // This method looks in LessonRepository by teacherId and lessonTime and returns true if the slot is occupied.
//    public boolean check(LocalDateTime time, int id) {
//        return !lessonRepository.findAllByTeacherIdAndLessonTime(id, time).isEmpty();
//    }
    // Deletes a record in the "lessons" module by ID.
    // Before calling deleteById, the method reads the entity to ensure the deletion passes through the service layer and fails clearly if the ID is incorrect.
    public void deleteById(Integer id) {
        Lesson existing = getById(id);
        lessonRepository.deleteById(id);
    }
    // Searches for records in the "lessons" module by condition: teacher.
    // The actual query is performed by `lessonRepository.findAllByTeacherId`, and the controller receives the final result.
    public List<Lesson> findAllByTeachId(int id) {
        return lessonRepository.findAllByTeacherId(id);
    }

    private List<TimeOfTheWeek> getTeacherFreeTimes(int teacherId) {
        Teacher teacher = teacherService.getById(teacherId);
        if (teacher.getFreeTimeIds() == null || teacher.getFreeTimeIds().isBlank()) {
            return Collections.emptyList();
        }
        return theWeekService.getTimesOfTheWeekThroughIds(teacher.getFreeTimeIds());
    }
    // Searches for a student's lesson on a specific date and time.
    // Such a search is needed when a UI action comes as a selected time slot.
//    public Lesson findByStudentIdAndTime(LocalDateTime time, int id) {
//        return lessonRepository.findByLessonTimeAndStudentId(time, id);
//    }
    // Returns lessons for a single teacher-student pair within one weekly slot.
    // The background lesson manager uses this to continue the specific chain of WILL-lessons.
    public List<Lesson> findAllByTeachIdAndStIdAndWeekId(int tId, int sId, int wId){
        return lessonRepository.findAllByTeacherIdAndStudentIdAndTimeOfTheWeek(tId, sId, wId);
    }
    public List<Lesson> findAllByStudentIdAndLessonTimeAfterAndLessonTimeBefore(int studentId, LocalDateTime after, LocalDateTime before){
        return lessonRepository.findAllByStudentIdAndLessonTimeAfterAndLessonTimeBefore(studentId, after, before);
    }
    // Deletes lessons associated with a selected weekly slot.
    // This method is needed when an admin or teacher removes available time and dependent lessons need to be cleared.
//    public void deleteAllByTimeOfTheWeekId(int id) {
//        lessonRepository.removeAllByTimeOfTheWeekId(id);
//    }
    // Deletes future lessons for a specific student-teacher pair in a given slot.
    // This allows removing a course without affecting past lessons.
    public void deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(int stId, int teachId, int tswId, LocalDateTime now) {
        lessonRepository.removeAllByStudentIdAndTeacherIdAndTimeOfTheWeekAndLessonTimeAfter(stId, teachId, tswId, now);
    }
    // Returns all lessons between a specific teacher and student.
    // These data are used by teacherStudentLessons/studentLessons pages for the calendar of a single study pair.
    public List<Lesson> findAllByIdTandIdSt(int idT, int idSt) {
        return lessonRepository.findAllByTeacherIdAndStudentId(idT, idSt);
    }

    private boolean shouldUseNextSlot(String status) {
        return "TEACHER".equals(status) || "TEACHERANDSTUDENT".equals(status);
    }

    public LocalDateTime manageOneTime(TimeOfTheWeek empTFT, LocalDateTime finalNow, String status){
        LocalDateTime baseTime = finalNow == null ? LocalDateTime.now() : finalNow;
        LocalDateTime firstAllowedTime = shouldUseNextSlot(status) ? baseTime : baseTime.plusHours(12);

        int daysToAdd = empTFT.getDayOfTheWeek() - firstAllowedTime.getDayOfWeek().getValue();
        if (daysToAdd < 0) {
            daysToAdd += 7;
        }

        LocalDateTime time = firstAllowedTime.plusDays(daysToAdd)
                .withHour(empTFT.getTimeOfTheDay())
                .withMinute(empTFT.getMinute())
                .withSecond(0)
                .withNano(0);

        if (!time.isAfter(firstAllowedTime)) {
            time = time.plusWeeks(1);
        }
        return time;
    }
    public String manageTimeOfTheLesson(Lesson lesson){
        String timeOfTheLesson = "";
        if (lesson.getLessonTime().getMinute() == 0) {
            timeOfTheLesson += String.format("%02d", lesson.getLessonTime().getDayOfMonth()) + "." + String.format("%02d", lesson.getLessonTime().getMonthValue()) + " " + lesson.getLessonTime().getHour() + ":00";
        } else {
            timeOfTheLesson += String.format("%02d", lesson.getLessonTime().getDayOfMonth()) + "." + String.format("%02d", lesson.getLessonTime().getMonthValue()) + " " + lesson.getLessonTime().getHour() + ":" + lesson.getLessonTime().getMinute();
        }
        return timeOfTheLesson;
    }
    public String manageDurationOfTheLesson(Lesson lesson, int i){
        LocalDateTime durationTime = lesson.getLessonTime();
        durationTime = durationTime.plusMinutes(i * 30);
        String durationTimeStr = "";
        if (durationTime.getMinute() == 0) {
            durationTimeStr = String.format("%02d", durationTime.getDayOfMonth()) + "." + String.format("%02d", durationTime.getMonthValue()) + " " + durationTime.getHour() + ":00";
        } else {
            durationTimeStr = String.format("%02d", durationTime.getDayOfMonth()) + "." + String.format("%02d", durationTime.getMonthValue()) + " " + durationTime.getHour() + ":" + durationTime.getMinute();
        }
        return durationTimeStr;
    }

    private void addLessonSegments(HashMap<String, LessonAdminLessonsDTO> lessons, Lesson lesson) {
        LessonAdminLessonsDTO lessonDTO = lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson);
        lessons.put(manageTimeOfTheLesson(lesson), lessonDTO);

        for (int i = 1; i < lesson.getDuration() * 2; i++) {
            lessons.put(manageDurationOfTheLesson(lesson, i), lessonDTO);
        }
    }

    // Assembles the teacher's calendar: days, lessons, continuation cells for long lessons, and available slots for rescheduling.
    // The method returns one map with both lesson starts and continuation cells so templates can read only "lessons".
    public HashMap<String, LessonAdminLessonsDTO> compileLessonsForTeacher(int teacherId) throws JsonProcessingException {
        List<Lesson> teacherLessons1 = findAllByTeachId(teacherId);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for (Lesson lesson : teacherLessons1) {
            addLessonSegments(lessonHashMap, lesson);
        }
        //////////////////
        LocalDateTime now2 = LocalDateTime.now();

        final LocalDateTime[] t = {now2};
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val -> less.add(val)); ////////////////////

/////////
        final List<LessonAdminLessonsDTO>[] teacherLessons = new List[]{less};
        HashMap<LocalDateTime, TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
        List<TimeOfTheWeek> teacherFreeTimeSlots = theWeekService.getAll();
        for (int i = 0; i < 3; i++) {
            LocalDateTime finalNow = now2;
            teacherFreeTimeSlots.stream().forEach(empTFT -> {
                t[0] = manageOneTime(empTFT, finalNow, "TEACHER");
                teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT, TimeOfTheWeekAdminLessonsDTO.class));

            });
            now2 = now2.plusWeeks(1);
        }


        Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
        String freeTimesJson = objectMapper.writeValueAsString(sortedFreeTimes);
        for (LessonAdminLessonsDTO val : lessonHashMap.values()) {
            val.getTeacher().setNotTakenTimes(freeTimesJson);
        }

        ///////////

        //////////////////

        return lessonHashMap;
    }

    // Assembles the calendar only for one teacher-student pair.
    // It filters lessons for this pair, adds teacher's available times, and returns structures for teacherStudentLessons/studentLessons.
    public HashMap<String, LessonAdminLessonsDTO> compileLessonsForStudentAndTeacher(int idT, int idSt) throws JsonProcessingException {

        List<Lesson> teacherLessons1 = findAllByIdTandIdSt(idT, idSt);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for (Lesson lesson : teacherLessons1) {
            addLessonSegments(lessonHashMap, lesson);
        }
        //////////////////
        LocalDateTime now2 = LocalDateTime.now();

        final LocalDateTime[] t = {now2};
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val -> less.add(val)); ////////////////////

/////////
        final List<LessonAdminLessonsDTO>[] teacherLessons = new List[]{new ArrayList()};
        findAllByTeachId(idT).stream().forEach(les -> teacherLessons[0].add(lessonMapper.mapLessonToLessonAdminLessonsDTO(les)));
        HashMap<LocalDateTime, TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
        List<TimeOfTheWeek> teacherFreeTimeSlots = theWeekService.getAll();
        for (int i = 0; i < 3; i++) {
            LocalDateTime finalNow = now2;
            teacherFreeTimeSlots.stream().forEach(empTFT -> {
                t[0] = manageOneTime(empTFT, finalNow, "TEACHERANDSTUDENT");
                teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT, TimeOfTheWeekAdminLessonsDTO.class));
            });
            now2 = now2.plusWeeks(1);
        }

        Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
        String freeTimesJson = objectMapper.writeValueAsString(sortedFreeTimes);
        for (LessonAdminLessonsDTO val : lessonHashMap.values()) {
            val.getTeacher().setNotTakenTimes(freeTimesJson);
        }

        ///////////
        //////////////////


        return lessonHashMap;
    }
    // Assembles the student's calendar with lessons from all their teachers.
    // For each teacher, it adds notTakenTimes so the student can see rescheduling options.
    public HashMap<String, LessonAdminLessonsDTO> compileLessonsForStudent(int stId) throws JsonProcessingException {
        List<Lesson> studentLessons = findAllByStudentId(stId);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for (Lesson lesson : studentLessons) {
            addLessonSegments(lessonHashMap, lesson);
        }

//////////////////////////

        LocalDateTime now2 = LocalDateTime.now();

        final LocalDateTime[] t = {now2};
        Set<TeacherAdminLessonsDTO> teachers = new HashSet<>();
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val -> less.add(val)); ////////////////////
        lessonHashMap.values().stream().forEach(les -> {
            if (!teachers.stream().map(teach -> teach.getId()).toList().contains(les.getTeacher().getId())) {
                teachers.add(les.getTeacher());
            }

        });
        for (TeacherAdminLessonsDTO teacher : teachers) {
            now2 = LocalDateTime.now();
            HashMap<LocalDateTime, TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                LocalDateTime finalNow = now2;
                getTeacherFreeTimes(teacher.getId()).forEach(empTFT -> {
                    t[0] = manageOneTime(empTFT, finalNow, "STUDENT");
                    teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT, TimeOfTheWeekAdminLessonsDTO.class));
                });
                now2 = now2.plusWeeks(1);
            }


            Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
            String freeTimesJson = objectMapper.writeValueAsString(sortedFreeTimes);
            for (LessonAdminLessonsDTO val : lessonHashMap.values()) {
                if (val.getTeacher().getId() == teacher.getId()) {
                    val.getTeacher().setNotTakenTimes(freeTimesJson);
                }
            }

        }

        return lessonHashMap;
    }
    // Assembles the admin version of the student's calendar.
    // The structure is the same as in the student cabinet, but data is prepared for the administrative template.
    public HashMap<String, LessonAdminLessonsDTO> compileLessonsForStudentInAdmin(int stId) throws JsonProcessingException {

        List<Lesson> studentLessons = findAllByStudentId(stId);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for (Lesson lesson : studentLessons) {
            addLessonSegments(lessonHashMap, lesson);
        }


        LocalDateTime now2 = LocalDateTime.now();

        final LocalDateTime[] t = {now2};
        Set<TeacherAdminLessonsDTO> teachers = new HashSet<>();
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val -> less.add(val)); ////////////////////
        lessonHashMap.values().stream().forEach(les -> {
            if (les.getTeacher() != null && !teachers.stream().map(TeacherAdminLessonsDTO::getId).toList().contains(les.getTeacher().getId())) {
                teachers.add(les.getTeacher());
            }
        });
        for (TeacherAdminLessonsDTO teacher : teachers) {
            now2 = LocalDateTime.now();
            final List<LessonAdminLessonsDTO>[] teacherLessons = new List[]{less.stream().filter(lesss -> lesss.getTeacher().equals(teacher)).toList()};
            HashMap<LocalDateTime, TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                LocalDateTime finalNow = now2;
                getTeacherFreeTimes(teacher.getId()).forEach(empTFT -> {
                    t[0] = manageOneTime(empTFT, finalNow, "STUDENTINADMIN");
                    teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT, TimeOfTheWeekAdminLessonsDTO.class));
                });
                now2 = now2.plusWeeks(1);
            }

            Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
            String freeTimesJson = objectMapper.writeValueAsString(sortedFreeTimes);
            for (LessonAdminLessonsDTO val : lessonHashMap.values()) {
                if (val.getTeacher().getId() == teacher.getId()) {
                    val.getTeacher().setNotTakenTimes(freeTimesJson);
                }
            }

        }



        return lessonHashMap;
    }



    // Checks if a new lesson interval overlaps with teacher's or student's lessons.
    // The method calculates the lesson end using duration, compares time intervals, and skips the lesson currently being edited.
    public boolean checkIfLessonOverlap(int tId, int sId, int lId, float dur, String date) {
        /////////////

        LocalDateTime newDate = LocalDateTime.now();
        if (LocalDateTime.now().getMonth().getValue() == 12 && Integer.parseInt(date.split(" ")[1].split("\\.")[1]) == 1) {
            newDate = LocalDateTime.now().withYear(LocalDateTime.now().getYear() + 1).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        } else {
            newDate = LocalDateTime.now().withYear(LocalDateTime.now().getYear()).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        }

        LocalDateTime time = newDate;
        LocalDateTime lessonFinish = time;
        if (dur == (long) dur) {
            lessonFinish = lessonFinish.plusHours((long) dur);
        } else {
            lessonFinish = lessonFinish.plusHours((long) dur).plusMinutes(30);
        }

        /////////////////

        AtomicBoolean overlap = new AtomicBoolean(false);
        List<Lesson> teacherLessons = findAllByTeacherIdAndLessonTimeAfterAndLessonTimeBefore(tId, newDate.minusHours(4), newDate.plusHours(4));
        LocalDateTime finalLessonFinish = lessonFinish;
        LocalDateTime finalTime = time;
        teacherLessons.stream().filter(les -> les.getId() != lId).forEach(l -> {
            LocalDateTime lFinish = l.getLessonTime();
            float lDur = l.getDuration();
            if (lDur == (long) lDur) {
                lFinish = lFinish.plusHours((long) lDur);
            } else {
                lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
            }
            if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime())) || l.getLessonTime().truncatedTo(ChronoUnit.MINUTES).equals(finalTime.truncatedTo(ChronoUnit.MINUTES))) {
                overlap.set(true);
            }
        });

////////////////////////////////////
        List<Lesson> studentLessons = findAllByStudentIdAndLessonTimeAfterAndLessonTimeBefore(sId, newDate.minusHours(4), newDate.plusHours(4));
        studentLessons.stream().filter(les -> les.getId() != lId).forEach(l -> {
            LocalDateTime lFinish = l.getLessonTime();
            float lDur = l.getDuration();
            if (lDur == (long) lDur) {
                lFinish = lFinish.plusHours((long) lDur);
            } else {
                lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
            }
            if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime())) || l.getLessonTime().truncatedTo(ChronoUnit.MINUTES).equals(finalTime.truncatedTo(ChronoUnit.MINUTES))) {
                overlap.set(true);
            }
        });
        return overlap.get();
    }

    // Checks if a lesson can be created or rescheduled for the selected day and time.
    // Returns a specific Statuses: past date, too late, overlap, participant not selected, course or duration not selected.
    public Statuses checkIfLessonValid(int tId, int sId, int lId, int cId, float dur, String date) {
        LocalDateTime newDate = LocalDateTime.now();
        if (LocalDateTime.now().getMonth().getValue() == 12 && Integer.parseInt(date.split(" ")[1].split("\\.")[1]) == 1) {
            newDate = LocalDateTime.now().withYear(LocalDateTime.now().getYear() + 1).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        } else {
            newDate = LocalDateTime.now().withYear(LocalDateTime.now().getYear()).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        }
        LocalDateTime lessonFinish = newDate;
        if (dur == (long) dur) {
            lessonFinish = lessonFinish.plusHours((long) dur);
        } else {
            lessonFinish = lessonFinish.plusHours((long) dur).plusMinutes(30);
        }
        //////////////
        if (newDate.isBefore(LocalDateTime.now())) {
            return Statuses.LESSON_BEFORE_NOW;
        } else if (lessonFinish.isAfter(newDate.withHour(22).withMinute(1))) {
            return Statuses.LESSON_AFTER_ACCEPTED_TIME;
        } else if (checkIfLessonOverlap(tId, sId, lId, dur, date)) {
            return Statuses.LESSON_OVERLAP;
        }else if (lId==0) {
            return Statuses.ITEM_NOT_PICKED;
        }else if (dur==0) {
            return Statuses.ITEM_NOT_PICKED;
        } else if (cId == 0) {
            return Statuses.ITEM_NOT_PICKED;
        } else if (sId == 0) {
            return Statuses.STUDENT_NOT_FOUND;
        }else if (tId == 0) {
            return Statuses.TEACHER_NOT_FOUND;
        } else if (date == "") {
            return Statuses.ITEM_NOT_PICKED;
        }else if (studentService.getById(sId)==null) {
            return Statuses.STUDENT_NOT_FOUND;
        } else if (teacherService.getById(sId)==null) {
            return Statuses.TEACHER_NOT_FOUND;
        }else if (courseService.getById(sId)==null) {
            return Statuses.COURSE_NOT_FOUND;
        }else {
            return Statuses.SUCCESS;
        }

    }

    // Checks a series of recurring lessons for several weeks ahead.
    // For each repetition, it calls checkIfLessonValid and returns the first error status or SUCCESS along with the last checked date.
    public List<Object> checkIfLessonValidWithReputitions(int tId, int sId, int cId, float dur, String date, int repetitions) {
    // For each repetition, it calls checkIfLessonValid and returns the first error status or SUCCESS.
        return checkIfLessonValidWithReputitions(tId, sId, cId, -1, dur, date, repetitions);
    }

    // Checks a series of recurring lessons for several weeks ahead.
    // For each repetition, it calls checkIfLessonValid and returns the first error status or SUCCESS along with the last checked date.
    public List<Object> checkIfLessonValidWithReputitions(int tId, int sId, int cId, int idLes, float dur, String date, int repetitions) {
        String checkingDate = date;
        LocalDateTime time = LocalDateTime.now();
        if (LocalDateTime.now().getMonth().getValue() == 12 && Integer.parseInt(date.split(" ")[1].split("\\.")[1]) == 1) {
            time = LocalDateTime.now().withYear(LocalDateTime.now().getYear() + 1).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        } else {
            time = LocalDateTime.now().withYear(LocalDateTime.now().getYear()).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        }
        Statuses status = Statuses.GENERAL;
        for (int i = 0; i < repetitions; i++) {
            LocalDateTime checkingTime = time.plusWeeks(i);
            checkingDate = checkingTime.getDayOfWeek() + " " + checkingTime.getDayOfMonth() + "." + checkingTime.getMonth().getValue() + " " + checkingTime.getHour() + ":" + checkingTime.getMinute();
            Statuses checkingSt = checkIfLessonValid(tId, sId, idLes, cId, dur, checkingDate);
            if (checkingSt == Statuses.SUCCESS) {
                status = checkingSt;
            } else {
                List<Object> retValues = new ArrayList<>();
                retValues.add(checkingSt);
                retValues.add(time);
                return retValues;
            }
        }
        List<Object> retValues = new ArrayList<>();
        retValues.add(status);
        retValues.add(time);
        return retValues;
    }
}
