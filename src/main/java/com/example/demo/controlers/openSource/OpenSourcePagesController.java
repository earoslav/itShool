package com.example.demo.controlers.openSource;

import com.example.demo.services.program.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

// OpenSourcePagesController handles web pages for the "public pages and registration" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/openSource")
public class OpenSourcePagesController {
    private final CourseService courseService;

    // Receives dependency through Spring: CourseService.
    @Autowired
    public OpenSourcePagesController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Opens the GET /homepage route and prepares data for the "openSource/openSourceHomepage/homepage" template.
    @GetMapping("/homepage")
    public String gotoHomePage(Model model) {
        courseService.manageGoToPublicHomepage(model);
        return "openSource/openSourceHomepage/homepage";
    }
    @GetMapping("/homepage/{output}")
    public String gotoHomePageWithOutput(Model model, @PathVariable("output") String output) {
        courseService.manageGoToPublicHomepageWithOutput(model, output);
        return "openSource/openSourceHomepage/homepage";
    }

    // Opens the GET /courses route and prepares data for the "openSource/openSourceOther/courses" template.
    @GetMapping("/courses")
    public String gotoCourses (Model model){
        courseService.manageGoToPublicCourses(model);
        return "openSource/openSourceOther/courses";
    }
}
