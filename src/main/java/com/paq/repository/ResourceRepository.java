/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository;

import com.paq.pojo.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface ResourceRepository {

    List<Resource> getResources(Map<String, String> params);

    Resource getResourceById(int id);

    List<Resource> getRelatedResources(int resourceId);

}
