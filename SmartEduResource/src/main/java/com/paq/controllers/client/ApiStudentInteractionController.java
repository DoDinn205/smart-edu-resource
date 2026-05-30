/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.controllers.client;

import com.paq.pojo.request.ReqInteractionDTO;
import com.paq.pojo.response.ResInteractionDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.StudentInteractionService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiStudentInteractionController {

    @Autowired
    private StudentInteractionService interactionService;

    @GetMapping("/student/resources/{resourceId}/interactions")
    public ResponseEntity<ResResponse<List<ResInteractionDTO>>> getInteractions(
            @PathVariable("resourceId") int resourceId) {

        ResResponse<List<ResInteractionDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Get interactions successfully");
        res.setData(this.interactionService.getInteractionsByResourceId(resourceId));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/secure/student/resources/{resourceId}/interactions")
    public ResponseEntity<ResResponse<ResInteractionDTO>> createInteraction(
            @PathVariable("resourceId") int resourceId,
            @RequestBody ReqInteractionDTO request,
            Principal principal) {

        ResResponse<ResInteractionDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Create interaction successfully");
        res.setData(this.interactionService.createInteraction(
                principal.getName(),
                resourceId,
                request
        ));

        return ResponseEntity.ok(res);
    }

    @PutMapping("/secure/student/interactions/{interactionId}")
    public ResponseEntity<ResResponse<ResInteractionDTO>> updateInteraction(
            @PathVariable("interactionId") int interactionId,
            @RequestBody ReqInteractionDTO request,
            Principal principal) {

        ResResponse<ResInteractionDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Update interaction successfully");
        res.setData(this.interactionService.updateInteraction(
                principal.getName(),
                interactionId,
                request
        ));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/secure/student/interactions/{interactionId}")
    public ResponseEntity<ResResponse<ResInteractionDTO>> deleteInteraction(
            @PathVariable("interactionId") int interactionId,
        Principal principal) {

        ResResponse<ResInteractionDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delete interaction successfully");
        res.setData(this.interactionService.deleteInteraction(
                principal.getName(),
                interactionId
        ));

        return ResponseEntity.ok(res);
    }
}
