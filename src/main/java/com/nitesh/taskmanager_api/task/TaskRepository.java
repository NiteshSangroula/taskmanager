package com.nitesh.taskmanager_api.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nitesh.taskmanager_api.user.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
}
