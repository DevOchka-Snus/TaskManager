package com.example.restspringapp.services;

import com.example.restspringapp.domain.task.TaskImage;

public interface ImageService {
    String upload(TaskImage image);
}
