package com.paq.service;

import com.paq.pojo.response.ResDashboardDTO;
import com.paq.pojo.response.ResLecturerDashboardDTO;
import com.paq.pojo.response.ResStudentDashboardDTO;

public interface DashboardService {

    ResDashboardDTO getAdminDashboard();

    ResLecturerDashboardDTO getLecturerDashboard();

    ResStudentDashboardDTO getStudentDashboard(String username);
}
