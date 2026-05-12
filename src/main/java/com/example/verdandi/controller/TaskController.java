package com.example.verdandi.controller;

import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Task;
import com.example.verdandi.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects/{projectId}/subprojects/{subprojectId}/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("")
    public String showTasks(@PathVariable int subprojectId,
                            @PathVariable int projectId,
                            Model model) {
        model.addAttribute("tasks", taskService.getTasksBySubproject(projectId, subprojectId));
        model.addAttribute("projectId", projectId);
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
                             Model model) {

        try {
            taskService.createTask(projectId, subprojectId, task);

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
                               Model model) {

        Task task = taskService.getSingleTask(projectId, subprojectId, taskId);

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
                             Model model) {

        try {
            taskService.updateTask(projectId, subprojectId, taskId, task);

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
                             @PathVariable int taskId) {

        taskService.deleteTask(projectId, subprojectId, taskId);
        return "redirect:/projects/" + projectId + "/subprojects/" + subprojectId + "/tasks";
    }
}
