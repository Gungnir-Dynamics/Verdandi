package com.example.verdandi.controller;

import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Task;
import com.example.verdandi.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects/{projectId}/subprojects/{subprojectId}/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping("")
    public String showTasks (@PathVariable int subprojectId,
                             @PathVariable int projectId,
                             Model model){
        model.addAttribute("tasks", taskService.getTasksBySubproject(projectId, subprojectId));
        model.addAttribute("projectId", projectId);
        return "tasks/task-list";
    }

    @GetMapping("/create")
    public String showCreateForm(@PathVariable int projectId,
                                 @PathVariable int subprojectId,
                                 Model model) {

        if (!model.containsAttribute("task")) {
            model.addAttribute("task", new Task());
        }
        model.addAttribute("projectId", projectId);
        model.addAttribute("subprojectId", subprojectId);

        return "tasks/create_task";
    }

    @PostMapping("/create")
    public String createTask(@PathVariable int projectId,
                             @PathVariable int subprojectId,
                             @ModelAttribute Task task,
                             RedirectAttributes redirectAttributes) {

        try {
            taskService.createTask(projectId, subprojectId, task);

            return "redirect:/projects/" + projectId + "/subprojects/" + subprojectId + "/tasks";

        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("task", task);
            return "redirect:/projects/" + projectId + "/subprojects/" + subprojectId + "/tasks/create";
        }
    }
}
