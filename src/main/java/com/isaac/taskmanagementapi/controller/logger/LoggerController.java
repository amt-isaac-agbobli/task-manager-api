package com.isaac.taskmanagementapi.controller.logger;

import com.isaac.taskmanagementapi.exception.HttpException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@SecurityRequirement(name = "bearer-key")
public class LoggerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerController.class);

    @GetMapping(value = "/log", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> loggerTest(@RequestParam("q") String q){
        Map<String, String> response = new HashMap<>();
        try {
            LOGGER.info("started...");
            response.put("message", "normal flow of the application");
            if(q.equalsIgnoreCase("exception")){
                throw new HttpException("This is an exception message", HttpStatus.BAD_REQUEST);
            }
            LOGGER.info("before sending response");
            return ResponseEntity.ok(response);
        }catch (Exception ex){
            LOGGER.error("Exception: {}", ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}