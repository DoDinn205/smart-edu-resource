/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service.impl;

import com.paq.pojo.Resource;
import com.paq.repository.ResourceRepository;
import com.paq.service.StudentResourceService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class StudentResourceServiceImpl implements StudentResourceService {

    @Autowired
    private ResourceRepository resourceRepo;

    @Override
    public List<Resource> getResources(Map<String, String> params) {
        return this.resourceRepo.getResources(params);
    }

    @Override
    public Resource getResourceById(int id) {
        return this.resourceRepo.getResourceById(id);
    }

    @Override
    public List<Resource> getRelatedResources(int resourceId) {
        return this.resourceRepo.getRelatedResources(resourceId);
    }

}
