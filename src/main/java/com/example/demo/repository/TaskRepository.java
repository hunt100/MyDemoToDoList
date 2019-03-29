package com.example.demo.repository;

import com.example.demo.entity.MyUser;
import com.example.demo.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
     List<Task> getTasksByMyUser (MyUser myUser);
}
