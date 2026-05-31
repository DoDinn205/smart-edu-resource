/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.controllers.client;

import com.paq.pojo.response.ResLecturerDashboardDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.pojo.response.ResStudentDashboardDTO;
import com.paq.service.DashboardService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/api/secure/student")
public class ApiStudentDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<ResResponse<ResStudentDashboardDTO>> getStudentDashboard(Principal principal) {
        ResResponse<ResStudentDashboardDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thống kê dashboard sinh viên thành công");
        res.setData(this.dashboardService.getStudentDashboard(principal.getName()));

        return ResponseEntity.ok(res);
    }
}
