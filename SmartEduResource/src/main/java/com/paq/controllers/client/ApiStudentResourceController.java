/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.controllers.client;

import com.paq.pojo.response.ResResourceDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.StudentResourceService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/api/student")
public class ApiStudentResourceController {

    @Autowired
    private StudentResourceService resourceService;

    @GetMapping("/resources")
    public ResponseEntity<ResResponse<List<ResResourceDTO>>> getResources(
            @RequestParam Map<String, String> params) {

        ResResponse<List<ResResourceDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Get resources successfully");
        res.setData(this.resourceService.getResources(params));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/resources/{id}")
    public ResponseEntity<ResResponse<ResResourceDTO>> getResourceDetail(
            @PathVariable(value = "id") int id) {

        ResResponse<ResResourceDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Get resource detail successfully");
        res.setData(this.resourceService.getResourceById(id));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/resources/{id}/related")
    public ResponseEntity<ResResponse<List<ResResourceDTO>>> getRelatedResources(
            @PathVariable(value = "id") int id) {

        ResResponse<List<ResResourceDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Get related resources successfully");
        res.setData(this.resourceService.getRelatedResources(id));

        return ResponseEntity.ok(res);
    }

}
