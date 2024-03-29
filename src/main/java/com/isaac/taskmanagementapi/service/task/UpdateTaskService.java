package com.isaac.taskmanagementapi.service.task;
import com.isaac.taskmanagementapi.dto.task.UpdateTaskRequest;
import com.isaac.taskmanagementapi.entity.Task;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.enums.Status;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.TaskRepository;
import com.isaac.taskmanagementapi.repository.UserRepository;
import com.isaac.taskmanagementapi.util.email.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UpdateTaskService {
    private final TaskRepository taskRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private static final String MESSAGE = "message";
    @Autowired
    public UpdateTaskService(TaskRepository taskRepository,
                             EmailService emailService,
                             UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }
    public Object updateTask(UpdateTaskRequest request, int taskId, int userId) {

        Task taskExit = taskRepository.findById(taskId).orElse(null);
        if(taskExit == null)
            throw new HttpException("Task does not exist to update", HttpStatus.NOT_FOUND);

        checkOwnerOfTask(userId, taskExit);

        taskExit.setTitle(request.getTitle());
        taskExit.setDescription(request.getDescription());
        taskExit.setDueDate(request.getDueDate());
        taskRepository.save(taskExit);

        return Map.of(MESSAGE, "Task updated successfully");
    }

    public Object reassignTask(int taskId, int userId, int assignedTo) {
        Task taskExit = taskRepository.findById(taskId).orElse(null);
        if(taskExit == null)
            throw new HttpException("Task does not exist", HttpStatus.NOT_FOUND);

        checkOwnerOfTask(userId, taskExit);

        User assignee = assignee(assignedTo);
        taskExit.setAssignedTo(User.builder().id(assignedTo).build());
        taskRepository.save(taskExit);

        sendEmailNotification(assignee, taskExit);

        return Map.of(MESSAGE, "Task reassigned successfully");
    }

    public Object updateTaskStatus(int taskId, String status) {
        Task taskExit = taskRepository.findById(taskId).orElse(null);
        if(taskExit == null)
            throw new HttpException("Task does not exist", HttpStatus.NOT_FOUND);

        checkIfStatusIsValid(status);

        taskExit.setStatus(Status.valueOf(status));
        taskRepository.save(taskExit);

        return Map.of(MESSAGE, "Task status updated successfully");
    }

    public Object deleteTask(int taskId, int userId) {
        Task taskExit = taskRepository.findById(taskId).orElse(null);
        if(taskExit == null)
            throw new HttpException("Task does not exist to delete", HttpStatus.NOT_FOUND);

        checkOwnerOfTask(userId, taskExit);

        taskRepository.delete(taskExit);

        return Map.of(MESSAGE, "Task deleted successfully");
    }


    private void checkOwnerOfTask(int userId, Task taskExit) {
        if(taskExit.getCreatedBy().getId() != userId)
            throw new HttpException("You are not authorized to update this task",
                    HttpStatus.UNAUTHORIZED);
    }
    private User assignee(int assignedTo) {
        Optional<User> assigneeExit = userRepository.findById(assignedTo) ;
        if (assigneeExit.isEmpty()) {
            throw  new HttpException("User you are trying to assign the task to does not exist",
                    HttpStatus.NOT_FOUND);
        }
        return assigneeExit.get();
    }

    private void sendEmailNotification(User assignee, Task task) {
        this.emailService.sendEmail(assignee.getEmail(), "Task Assigned",
                "You have been assigned a new task with title " + task.getTitle()
                        + " and due date " + task.getDueDate());
    }

    private void checkIfStatusIsValid(String statusRequest) {
         String status = statusRequest.toUpperCase();
        if(!status.equals("PENDING") && !status.equals("IN_PROGRESS") && !status.equals("COMPLETED"))
            throw new HttpException("Invalid status", HttpStatus.BAD_REQUEST);
    }

}
