package com.coopeuch.persistence;

import org.springframework.data.repository.CrudRepository;

import com.coopeuch.model.TaskEntity;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
    
}
