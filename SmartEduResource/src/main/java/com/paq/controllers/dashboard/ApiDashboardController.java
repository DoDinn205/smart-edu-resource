package com.paq.controllers.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.response.ResDashboardDTO;
import com.paq.pojo.response.ResLecturerDashboardDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.pojo.response.ResStudentDashboardDTO;
import com.paq.service.DashboardService;
import java.security.Principal;

@RestController
@RequestMapping("/api/secure")
public class ApiDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin/dashboard")
    public ResponseEntity<ResResponse<ResDashboardDTO>> getAdminDashboard() {
        ResResponse<ResDashboardDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thống kê dashboard thành công");
        res.setData(this.dashboardService.getAdminDashboard());

        return ResponseEntity.ok(res);
    }

    @GetMapping("/lecturer/dashboard")
    public ResponseEntity<ResResponse<ResLecturerDashboardDTO>> getLecturerDashboard() {
        ResResponse<ResLecturerDashboardDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thống kê dashboard giảng viên thành công");
        res.setData(this.dashboardService.getLecturerDashboard());

        return ResponseEntity.ok(res);
    }

    @GetMapping("/student/dashboard")
    public ResponseEntity<ResResponse<ResStudentDashboardDTO>> getStudentDashboard(Principal principal) {
        ResResponse<ResStudentDashboardDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thống kê dashboard sinh viên thành công");
        res.setData(this.dashboardService.getStudentDashboard(principal.getName()));

        return ResponseEntity.ok(res);
    }
}
