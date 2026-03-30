package com.nitesh.taskmanager_api.task;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nitesh.taskmanager_api.task.dto.TaskRequest;
import com.nitesh.taskmanager_api.task.dto.TaskResponse;
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
                task.getTitle(), task.getDescription(), task.getStatus());

    }

    public TaskResponse createTask(TaskRequest taskRequest) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        if (!userRepository.existsByEmail(email)) {
            throw new RuntimeException("User not found");
        }

        User user = userRepository.findByEmail(email);

        Task task = Task.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .status(TaskStatus.TODO)
                .user(user)
                .build();

        Task createdTask = taskRepository.save(task);

        return mapToResponse(createdTask);
    }

    public List<TaskResponse> getAllTasks() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        if (!userRepository.existsByEmail(email)) {
            throw new RuntimeException("User not found");
        }

        User user = userRepository.findByEmail(email);

        return StreamSupport
                .stream(taskRepository.findByUser(user).spliterator(), false)
                .map(this::mapToResponse)
                .toList();
    }

}
