package com.isaac.taskmanagementapi.service.task;

import com.isaac.taskmanagementapi.dto.task.AddTaskRequest;
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
public class AddTaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private  final EmailService emailService;
    @Autowired
    public AddTaskService(TaskRepository taskRepository,
                          UserRepository userRepository,
                          EmailService emailService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public Object addTask(AddTaskRequest request, User user) {
        int assignedTo = request.getAssignedTo() == 0 ? user.getId()
                : request.getAssignedTo();

        User assignee = assignee(assignedTo);


        Task task = Task.builder()
                     .title(request.getTitle())
                     .description(request.getDescription())
                     .dueDate(request.getDueDate())
                     .status(Status.PENDING)
                     .createdBy(user)
                     .assignedTo(User.builder().id(assignedTo).build())
                     .build();
                taskRepository.save(task);

                sendEmailNotification(assignee, task);

        return Map.of( "message", "Task created successfully");
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
}
