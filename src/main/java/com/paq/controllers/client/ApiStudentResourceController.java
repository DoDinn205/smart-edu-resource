/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.controllers.client;

import com.paq.pojo.Resource;
import com.paq.service.StudentResourceService;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import static org.hibernate.Hibernate.map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class ApiStudentResourceController {

    @Autowired
    private StudentResourceService resourceService;

    @GetMapping("/resources")
    public ResponseEntity<?> getResources(
            @RequestParam Map<String, String> params) {

        return ResponseEntity.ok(
                this.resourceService.getResources(params)
                        .stream()
                        .map(r -> resourceToMap(r))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/resources/{id}")
    public ResponseEntity<?> getResourceDetail(
            @PathVariable(value = "id") int id) {

        Resource r = this.resourceService.getResourceById(id);

        if (r == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resourceToMap(r));
    }

    @GetMapping("/resources/{id}/related")
    public ResponseEntity<?> getRelatedResources(
            @PathVariable(value = "id") int id) {

        return ResponseEntity.ok(
                this.resourceService.getRelatedResources(id)
                        .stream()
                        .map(r -> resourceToMap(r))
                        .collect(Collectors.toList())
        );
    }

    private Map<String, Object> resourceToMap(Resource r) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Object> m = new HashMap<>();
        m.put("id", r.getId());
        m.put("title", r.getTitle());
        m.put("description", r.getDescription());
        m.put("level", r.getLevel());
        

        return m;
    }
}
