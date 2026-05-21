package com.example.verdandi.controller;

import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Task;
import com.example.verdandi.model.User;
import com.example.verdandi.service.SubProjectService;
import com.example.verdandi.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects/{projectId}/subprojects/{subprojectId}/tasks")
public class TaskController {
    private final TaskService taskService;
    private final SubProjectService subProjectService;

    public TaskController(TaskService taskService, SubProjectService subProjectService) {
        this.taskService = taskService;
        this.subProjectService = subProjectService;
    }

    @GetMapping("")
    public String showTasks(@PathVariable int subprojectId,
                            @PathVariable int projectId,
                            Model model,
                            HttpSession session) {
        User user = (User) session.getAttribute("user");

        model.addAttribute("tasks", taskService.getTasksBySubproject(projectId, subprojectId, user));
        model.addAttribute("projectId", projectId);
        model.addAttribute("subprojectId", subprojectId);
        model.addAttribute("subproject", subProjectService.findSubProjectById(projectId, subprojectId, user));
        return "tasks/task-list";
    }

    @GetMapping("/create")
    public String showCreateForm(@PathVariable int projectId,
                                 @PathVariable int subprojectId,
                                 Model model) {

        model.addAttribute("task", new Task());

        model.addAttribute("projectId", projectId);
        model.addAttribute("subprojectId", subprojectId);

        return "tasks/create_task";
    }

    @PostMapping("/create")
    public String createTask(@PathVariable int projectId,
                             @PathVariable int subprojectId,
                             @ModelAttribute Task task,
                             Model model,
                             HttpSession session) {

        User user = (User) session.getAttribute("user");

        try {
            taskService.createTask(projectId, subprojectId, task, user);

            return "redirect:/projects/" + projectId + "/subprojects/" + subprojectId + "/tasks";

        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("task", task);
            model.addAttribute("projectId", projectId);
            model.addAttribute("subprojectId", subprojectId);

            return "tasks/create_task";
        }
    }

    @GetMapping("/{taskId}/edit")
    public String showEditForm(@PathVariable int projectId,
                               @PathVariable int subprojectId,
                               @PathVariable int taskId,
                               Model model,
                               HttpSession session) {

        User user = (User) session.getAttribute("user");

        Task task = taskService.getSingleTask(projectId, subprojectId, taskId, user);

        model.addAttribute("task", task);

        model.addAttribute("projectId", projectId);
        model.addAttribute("subprojectId", subprojectId);
        model.addAttribute("taskId", taskId);

        return "tasks/edit_task";
    }

    @PostMapping("/{taskId}/edit")
    public String updateTask(@PathVariable int projectId,
                             @PathVariable int subprojectId,
                             @PathVariable int taskId,
                             @ModelAttribute Task task,
                             Model model,
                             HttpSession session) {

        User user = (User) session.getAttribute("user");

        try {
            taskService.updateTask(projectId, subprojectId, taskId, task, user);

            return "redirect:/projects/" + projectId + "/subprojects/" + subprojectId + "/tasks";
        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("task", task);

            model.addAttribute("projectId", projectId);
            model.addAttribute("subprojectId", subprojectId);
            model.addAttribute("taskId", taskId);

            return "tasks/edit_task";
        }
    }

    @PostMapping("/{taskId}/delete")
    public String deleteTask(@PathVariable int projectId,
                             @PathVariable int subprojectId,
                             @PathVariable int taskId,
                             HttpSession session) {

        User user = (User) session.getAttribute("user");

        taskService.deleteTask(projectId, subprojectId, taskId, user);
        return "redirect:/projects/" + projectId + "/subprojects/" + subprojectId + "/tasks";
    }
}
