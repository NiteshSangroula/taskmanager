package com.nitesh.taskmanager_api.task;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nitesh.taskmanager_api.task.dto.*;
import com.nitesh.taskmanager_api.user.User;
import com.nitesh.taskmanager_api.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(), task.getTitle(), task.getDescription(), task.getStatus());

    }

    private User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not Found!"));
    }

    // 1. Create Task
    public TaskResponse createTask(TaskRequest taskRequest) {

        User user = getCurrentUser();

        Task task = Task.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .status(TaskStatus.TODO)
                .user(user)
                .build();

        Task createdTask = taskRepository.save(task);

        return mapToResponse(createdTask);
    }

    // 2. Get All Tasks
    public List<TaskResponse> getAllTasks() {
        User user = getCurrentUser();

        return StreamSupport
                .stream(taskRepository.findByUser(user).spliterator(), false)
                .map(this::mapToResponse)
                .toList();
    }

    // 3. Update Task
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task with " + id + " not found"));

        User user = getCurrentUser();

        if (task.getUser() != user) {
            throw new RuntimeException("Task doesn't belong to the user");
        }
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());

        Task createdTask = taskRepository.save(task);

        return mapToResponse(createdTask);
    }

    // 4. Delete Task
    public void deleteTask(Long id) {
        User user = getCurrentUser();

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task with " + id + " not found"));

        if (task.getUser() != user) {
            throw new RuntimeException("Task doesn't belong to the user");
        }

        taskRepository.deleteById(id);
    }

}
