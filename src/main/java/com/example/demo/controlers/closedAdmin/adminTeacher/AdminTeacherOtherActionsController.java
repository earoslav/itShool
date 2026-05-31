package com.example.demo.controlers.closedAdmin.adminTeacher;

import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.services.entities.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// AdminTeacherOtherActionsController handles web pages for the "teachers" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/admin")
public class AdminTeacherOtherActionsController {

    private final TeacherService teacherService;

    // Receives TeacherService dependency through Spring.
    // The class delegates request mapping and business flow control entirely to the service class.
    @Autowired
    public AdminTeacherOtherActionsController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Opens the GET /addTeacher route and prepares data for the "closedAdmin/adminTeachers/addNewTeacher" template.
    @GetMapping("/addTeacher")
    public String gotoCreateTeacher(Model model) {
        teacherService.manageGoToCreateTeacher(model, "");
        return "closedAdmin/adminTeachers/addNewTeacher";
    }

    // Opens the GET /addTeacher/{output} route and prepares data for the "closedAdmin/adminTeachers/addNewTeacher" template.
    @GetMapping("/addTeacher/{output}")
    public String gotoCreateTeacherWithOutput(@PathVariable("output") String output, Model model) {
        teacherService.manageGoToCreateTeacher(model, output);
        return "closedAdmin/adminTeachers/addNewTeacher";
    }

    // Opens the GET /addTeacher/{name}/{age}/{email}/{password}/{comment}/{tgUser}/{phonenumber}/{approved}/{courseIds}/{timeIds} route and prepares data for the "closedAdmin/adminTeachers/addNewTeacher" template.
    @GetMapping("/addTeacher/{name}/{age}/{email}/{password}/{comment}/{tgUser}/{phonenumber}/{approved}/{courseIds}/{timeIds}")
    public String gotoCreateTeacherFromEmail(Model model,
                                             @PathVariable(value = "name", required = false) String name,
                                             @PathVariable(value = "age", required = false) int age,
                                             @PathVariable(value = "email", required = false) String email,
                                             @PathVariable(value = "password", required = false) String password,
                                             @PathVariable(value = "comment", required = false) String comment,
                                             @PathVariable(value = "tgUser", required = false) String tgUser,
                                             @PathVariable(value = "phonenumber", required = false) String phonenumber,
                                             @PathVariable(value = "courseIds", required = false) String courseIds,
                                             @PathVariable(value = "timeIds", required = false) String timeIds) {

        teacherService.manageGoToCreateTeacherFromEmail(model, name, age, email, password, comment, tgUser, phonenumber, courseIds, timeIds);
        return "closedAdmin/adminTeachers/addNewTeacher";
    }

    // Creates data at the POST /addTeacher/{password} route in the "teachers" module.
    @PostMapping("/addTeacher")
    public String addTeacher(
            @ModelAttribute("teacher") TeacherDTO teacher,
            @RequestParam("password") String password,
            @RequestParam(value = "courseIds", required = false) List<Integer> coursesIds,
            @RequestParam(value = "freeTimeIds", required = false) List<Integer> freeTimesIds, Model model) {
        String res = teacherService.manageAddTeacher(teacher, password, coursesIds, freeTimesIds, model);
        if(res.equals("TEACHER_ADDED")){
            return "redirect:/admin/homepage/TEACHER_ADDED";
        }else{
            return "redirect:/admin/addTeacher/" + res;
        }

    }

    // Opens the GET /teachers route and prepares data for the "closedAdmin/adminTeachers/teachers" template.
    @GetMapping("/teachers")
    public String gotoTeachers(@RequestParam(value = "limit", required = false) String limit, Model model) {
        teacherService.manageGotoTeachers(model, limit);
        return "closedAdmin/adminTeachers/teachers";
    }

    // Opens the GET /teacher/{idTeach}/edit route and prepares data for the "closedAdmin/adminTeachers/editTeacher" template.
    @GetMapping("/teacher/{idTeach}/edit")
    public String gotoEditTeacher(@PathVariable("idTeach") int id, Model model) {
        teacherService.manageGoToEditTeacher(id, "", model);
        return "closedAdmin/adminTeachers/editTeacher";
    }

    // Opens the GET /teacher/{idTeach}/edit/{output} route and prepares data for the "closedAdmin/adminTeachers/editTeacher" template.
    @GetMapping("/teacher/{idTeach}/edit/{output}")
    public String gotoEditTeacherWithOutput(@PathVariable("idTeach") int id,
                                            @PathVariable("output") String output, Model model) {
        teacherService.manageGoToEditTeacher(id, output, model);
        return "closedAdmin/adminTeachers/editTeacher";
    }

    // Updates data at the POST /teacher/{id}/edit/{password} route in the "teachers" module.
    @PostMapping("/teacher/{id}/edit")
    public String updateTeacher(@PathVariable("id") int id,
                                @ModelAttribute("teacher") TeacherDTO teacher,
                                @RequestParam("password") String password,
                                @RequestParam(value = "freeTimeIds", required = false) List<Integer> freeTimeIds,
                                @RequestParam(value = "newCourseIds", required = false) List<Integer> newCourseIds,
                                Model model) {
        return "redirect:/admin/teacher/" + teacher.getId() + "/edit/" + teacherService.manageUpdateTeacher(id, teacher, password, freeTimeIds, newCourseIds);
    }

    // Deletes or disconnects data at the POST /teacher/{id}/delete route in the "teachers" module.
    @PostMapping("/teacher/{id}/delete")
    public String deleteTeacher(@PathVariable("id") int id, Model model) {
        teacherService.manageDeleteTeacher(id);
        return "redirect:/admin/teachers";
    }

    // Opens the GET /teacher/{teachId}/info route and prepares data for the "closedAdmin/adminTeachers/teacherInfo" template.
    @GetMapping("/teacher/{teachId}/info")
    public String gotoInfo(@PathVariable("teachId") int id, Model model) {
        teacherService.manageGoToInfo(id, "", model);
        return "closedAdmin/adminTeachers/teacherInfo";
    }

    // Opens the GET /teacher/{teachId}/info/{output} route and prepares data for the "closedAdmin/adminTeachers/teacherInfo" template.
    @GetMapping("/teacher/{teachId}/info/{output}")
    public String gotoInfoWithOutput(@PathVariable("teachId") int id, @PathVariable("output") String output, Model model) {
        teacherService.manageGoToInfo(id, output, model);
        return "closedAdmin/adminTeachers/teacherInfo";
    }

    // Updates data at the POST /teacher/{teachId}/setEarnedMoneyToZero route in the "teachers" module.
    @PostMapping("/teacher/{teachId}/setEarnedMoneyToZero")
    public String setEarnedMoneyToZero(Model model, @PathVariable("teachId") int id) {
        teacherService.manageSetEarnedMoneyToZero(id);
        return "redirect:/admin/teacher/" + id + "/info";
    }

    // Updates data at the POST /teacher/{teachId}/setEarnedMoney route in the "teachers" module.
    @PostMapping("/teacher/{teachId}/setEarnedMoney")
    public String setEarnedMoney(Model model, @PathVariable("teachId") int id,
                                 @ModelAttribute("teacher") TeacherDTO teacherDTO) {
        teacherService.manageSetEarnedMoney(id, teacherDTO);
        return "redirect:/admin/teacher/" + id + "/info";
    }

    @PostMapping("/teacher/{teachId}/deleteCourse/{courseId}")
    public String deleteCourse(@PathVariable("courseId") int courseId, @PathVariable("teachId") int teachId) {
        return "redirect:/admin/teacher/" + teachId + "/edit/" + teacherService.manageDeleteCourse(courseId, teachId);
    }
}
