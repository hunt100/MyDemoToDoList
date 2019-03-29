package com.example.demo.controller;


import com.example.demo.entity.MyUser;
import com.example.demo.entity.SortDateTasks;
import com.example.demo.entity.Task;
import com.example.demo.repository.MyUserRepository;
import com.example.demo.service.MainService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    MainService mainService;

    @Autowired
    MyUserRepository myUserRepository;

    @GetMapping(path = {"/","/home"})
    public String myHomePage(Model model) {
        List<MyUser> allUsers = mainService.getAllUser();
        model.addAttribute("allUsers", allUsers);
        return "home";
    }

    @GetMapping(path = "/addTask")
    public String taskPage() {
        return "addTaskPage";
    }

    @RequestMapping(value = "/addTask", method = RequestMethod.POST)
    public String addTask(@RequestParam(name = "taskName")String taskName,
                          @RequestParam(name = "taskDescription")String taskDescription,
                          @RequestParam(name = "taskDay")String taskDate,
                          @RequestParam(name = "taskTime")String taskTime,
                          Authentication authentication) {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = formatter.parse(taskDate+ " " + taskTime);
            System.err.println(date);
            MyUser currentUser = mainService.getCurrentUser(authentication.getName());
            Task task = new Task(0L, taskName, taskDescription, 0, date, currentUser);
            mainService.createTask(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/showNew";
    }

    @RequestMapping(value = "/task/{taskId}", method=RequestMethod.GET)
    public String getTaskByIdPage(@PathVariable String taskId, Model model, Authentication authentication) {
        MyUser currentUser = mainService.getCurrentUser(authentication.getName());
        List<Task> tasks = mainService.getTasksByUser(currentUser);
        boolean isUserTask = false;

        Task task = mainService.getTaskById(Long.parseLong(taskId));

        if (task == null) {
            System.err.println("not found");
            String errorMsg = "Sorry, this task not found";
            model.addAttribute("errorMsg",errorMsg);
            return "task";
        }

        for (Task t : tasks) {
            if (task.getId() == t.getId()) {
                isUserTask = true;
                break;
            }
        }

        if(!isUserTask) {
            System.err.println("not this user task");
            String errorMsg = "Nice try, this is not your task";
            model.addAttribute("errorMsg",errorMsg);
            return "task";
        }

        System.err.println(task.getId() + " " + task.getTaskName());
        model.addAttribute("thisTask", task);
        return "task";
    }

    @GetMapping(path = "/edit/{taskId}")
    public String showUpdateForm(@PathVariable("taskId") long taskId, Model model) {
        Task tempTask = mainService.getTaskById(taskId);
        if (tempTask == null) {
            System.err.println("not found");
            String errorMsg = "Sorry, this task not found";
            model.addAttribute("errorMsg",errorMsg);
            return "edit";
        }
        System.err.println(tempTask.getId() + " " + tempTask.getTaskName());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String newDate = df.format(tempTask.getDate());
        df = new SimpleDateFormat("HH:mm");
        String newTime = df.format(tempTask.getDate());
        model.addAttribute("newDate", newDate);
        model.addAttribute("newTime", newTime);
        model.addAttribute("thisTask", tempTask);
        return "edit";
    }

    @PostMapping(path = "/update/{taskId}")
    public String updateTask(@PathVariable("taskId") long taskId,
                             @RequestParam(name = "taskName")String taskName,
                             @RequestParam(name = "taskDescription")String taskDescription,
                             @RequestParam(name = "taskDay")String taskDate,
                             @RequestParam(name = "taskTime")String taskTime,
                             @RequestParam(name = "status") int status) {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = formatter.parse(taskDate + " " + taskTime);
            Task tempTask = new Task();
            tempTask.setId(taskId);
            tempTask.setStatus(status);
            tempTask.setTaskName(taskName);
            tempTask.setTaskDescription(taskDescription);
            tempTask.setDate(date);
            mainService.updateTask(tempTask);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/showNew";
    }

    @GetMapping(path = "/delete/{taskId}")
    public String deleteUser(@PathVariable("taskId") long taskId) {
        mainService.deleteTask(taskId);
        System.err.println("Deleted task with taskId " + taskId);
        return "redirect:/showNew";
    }

    @PostMapping(path = "/completeTask")
    public String updateStatus(@RequestParam("hiddenTaskId") Long taskId) {
        Task task = mainService.getTaskById(taskId);
        task.setStatus(1);
        mainService.updateTask(task);
        return "redirect:/showNew";
    }

    @GetMapping(path = "/showNew")
    public String getNewShowModel(Model model, Authentication authentication) {
        MyUser myUser = mainService.getCurrentUser(authentication.getName());
        List<Task> allTasks = mainService.getTasksByUser(myUser);
        Collections.sort(allTasks, new SortDateTasks());
        Map<Date,List<Task>> byDateTasks = allTasks
                                            .stream()
                                            .collect(Collectors.groupingBy((task -> DateUtils.truncate(task.getDate(),Calendar.DATE)), LinkedHashMap::new, Collectors.mapping(Function.identity(), Collectors.toList())));


        model.addAttribute("allTasks", byDateTasks);
        model.addAttribute("myUser", myUser);
        return "showNew";
    }

}
