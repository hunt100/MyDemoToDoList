package com.example.demo.service;

import com.example.demo.config.MyUserDetailsService;
import com.example.demo.entity.MyUser;
import com.example.demo.entity.Task;
import com.example.demo.repository.MyUserRepository;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MainService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MyUserRepository myUserRepository;

    @Transactional
    public void createTask(Task task) {
        taskRepository.save(task);
    }

    @Transactional
    public List<Task> getAllTask() {
        return (List<Task>) taskRepository.findAll();
    }

    @Transactional
    public List<Task> getTasksByUser(MyUser user) {
        return taskRepository.getTasksByMyUser(user);
    }

    @Transactional
    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        return task;
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public void updateTask(Task task) {
        Task taskFromDB = taskRepository.findById(task.getId()).orElse(null);
        taskFromDB.setTaskDescription(task.getTaskDescription());
        taskFromDB.setTaskName(task.getTaskName());
        taskFromDB.setDate(task.getDate());
        taskFromDB.setStatus(task.getStatus());
    }

    @Transactional
    public List<MyUser> getAllUser() {
        return (List<MyUser>) myUserRepository.findAll();
    }

    @Transactional
    public MyUser getCurrentUser(String login) {
        return myUserRepository.findByUserLogin(login).orElse(null);
    }

}
