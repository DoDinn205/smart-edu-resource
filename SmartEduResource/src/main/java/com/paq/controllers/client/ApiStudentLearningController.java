/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.controllers.client;

import com.paq.pojo.response.ResLearningLogDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.StudentLearningService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/api/secure/student")
public class ApiStudentLearningController {

    @Autowired
    private StudentLearningService learningService;

    @PostMapping("/resources/{id}/start")
    public ResponseEntity<ResResponse<ResLearningLogDTO>> startLearning(
            @PathVariable(value = "id") int id,
            Principal principal) {
        ResResponse<ResLearningLogDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Start learning successfully");
        res.setData(this.learningService.startLearning(principal.getName(), id));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/resources/{id}/complete")
    public ResponseEntity<ResResponse<ResLearningLogDTO>> completeLearning(
            @PathVariable(value = "id") int id,
            Principal principal) {
        ResResponse<ResLearningLogDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Complete learning successfully");
        res.setData(this.learningService.completeLearning(principal.getName(), id));

        return ResponseEntity.ok(res);
   
    }

    @GetMapping("/learning/history")
    public ResponseEntity<ResResponse<List<ResLearningLogDTO>>> getHistory(
            Principal principal) {
        ResResponse<List<ResLearningLogDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Get learning history successfully");
        res.setData(this.learningService.getHistory(principal.getName()));

        return ResponseEntity.ok(res);
    }

    
}
