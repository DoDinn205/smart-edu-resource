package com.paq.service;

import com.paq.pojo.response.ResDashboardDTO;
import com.paq.pojo.response.ResLecturerDashboardDTO;

public interface DashboardService {

    ResDashboardDTO getAdminDashboard();

    ResLecturerDashboardDTO getLecturerDashboard();
}
