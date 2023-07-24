package com.coopeuch.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.coopeuch.exception.DataValidationException;
import com.coopeuch.exception.TaskNotFoundException;
import com.coopeuch.model.APIResponse;
import com.coopeuch.model.TaskDTO;
import com.coopeuch.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<APIResponse> findAll() {

        List<TaskDTO> list = taskService.findAll();

        APIResponse apiResponse = new APIResponse();
        apiResponse.setData(list);
        apiResponse.setResponseCode(HttpStatus.OK);
        apiResponse.setMessage("Successfully retrieved data");

        return new ResponseEntity<>(apiResponse, apiResponse.getResponseCode());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<APIResponse> delete(@PathVariable String id) {
        try {
            taskService.delete(Long.parseLong(id));

            APIResponse apiResponse = new APIResponse();
            apiResponse.setResponseCode(HttpStatus.OK);
            apiResponse.setMessage("Successfully deleted");
            
            return new ResponseEntity<>(apiResponse, apiResponse.getResponseCode());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<APIResponse> create(@RequestParam("taskData") String taskDTO) {
        try {

            ObjectMapper mapper = new ObjectMapper();			
            TaskDTO t = mapper.readValue(taskDTO, TaskDTO.class);

            TaskDTO task = taskService.create(t) ;

            APIResponse apiResponse = new APIResponse();
            apiResponse.setData(task);
            apiResponse.setResponseCode(HttpStatus.OK);
            apiResponse.setMessage("Successfully created");

            return new ResponseEntity<>(apiResponse, apiResponse.getResponseCode());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<APIResponse> update(@RequestParam("taskData") String taskDTO) {        
        try {
        
            ObjectMapper mapper = new ObjectMapper();			
            TaskDTO t = mapper.readValue(taskDTO, TaskDTO.class);

            TaskDTO task = taskService.update(t);

            APIResponse apiResponse = new APIResponse();
            apiResponse.setData(task);
            apiResponse.setResponseCode(HttpStatus.OK);
            apiResponse.setMessage("Successfully updated");

            return new ResponseEntity<>(apiResponse, apiResponse.getResponseCode());
        
        } catch (Exception e) {
            if (e instanceof DataValidationException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } else if (e instanceof TaskNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }            
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> findById(@PathVariable String id) {
        try {                        
            
            TaskDTO taskDTO = taskService.findById(Long.parseLong(id));

            APIResponse apiResponse = new APIResponse();
            apiResponse.setData(taskDTO);
            apiResponse.setResponseCode(HttpStatus.OK);
            apiResponse.setMessage("Successfully retrieved data");

            return new ResponseEntity<>(apiResponse, apiResponse.getResponseCode());

        } catch (Exception e) {
            if (e instanceof TaskNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }            
        }
    }

    @GetMapping("/hello")
    public String hello() {
      return "hello world!";
    }
  

}
