package com.coopeuch.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coopeuch.exception.TaskNotFoundException;
import com.coopeuch.model.TaskDTO;
import com.coopeuch.model.TaskEntity;
import com.coopeuch.persistence.TaskRepository;

@Service
public class TaskService {
    
    final static String formatDate = "dd-MM-yyyy hh:mm:ss";

    @Autowired
    private TaskRepository taskRepository;

    public List<TaskDTO> findAll() {

        List<TaskDTO> listDTO = new ArrayList<>();

        Iterable<TaskEntity> list = taskRepository.findAll();

        for (TaskEntity task : list) {
            String formattedDate = new SimpleDateFormat(formatDate).format(task.getCreationDate());
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(task.getId());
            taskDTO.setDescription(task.getDescription());
            taskDTO.setCreationDate(formattedDate);
            taskDTO.setValid(task.isValid());
            listDTO.add(taskDTO);
        }

        return listDTO;
    }

    public TaskDTO findById(Long id) {

        TaskDTO taskDTO = new TaskDTO();

        TaskEntity task = taskRepository.findById(id)
        .orElseThrow(TaskNotFoundException::new);

        String formattedDate = new SimpleDateFormat(formatDate).format(task.getCreationDate());

        taskDTO.setId(task.getId());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setCreationDate(formattedDate);
        taskDTO.setValid(task.isValid());

        return taskDTO;

    }


    public TaskDTO create(TaskDTO taskDTO) {

        TaskEntity taskEntity = TaskEntity.builder()
        .description(taskDTO.getDescription())
        .creationDate(new Timestamp(System.currentTimeMillis()))
        .valid(taskDTO.isValid())
        .build();

        taskRepository.save(taskEntity);

        String formattedDate = new SimpleDateFormat(formatDate).format(taskEntity.getCreationDate());

        taskDTO = TaskDTO.builder()
        .id(taskEntity.getId())
        .description(taskEntity.getDescription())
        .creationDate(formattedDate)
        .valid(taskEntity.isValid())
        .build();

        return taskDTO;
    }

    public void delete(Long id) {

        taskRepository.findById(id)
        .orElseThrow(TaskNotFoundException::new);

        taskRepository.deleteById(id);
    }

    public TaskDTO update(TaskDTO taskDTO) {

        taskRepository.findById(taskDTO.getId())
        .orElseThrow(TaskNotFoundException::new);

        TaskEntity taskEntity = TaskEntity.builder()
        .id(taskDTO.getId())
        .description(taskDTO.getDescription())
        .creationDate(new Timestamp(System.currentTimeMillis()))
        .valid(true)
        .build();

        taskRepository.save(taskEntity);

        String formattedDate = new SimpleDateFormat(formatDate).format(taskEntity.getCreationDate());

        taskDTO = TaskDTO.builder()
        .id(taskEntity.getId())
        .description(taskEntity.getDescription())
        .creationDate(formattedDate)
        .valid(taskEntity.isValid())
        .build();

        return taskDTO;
    }

}
