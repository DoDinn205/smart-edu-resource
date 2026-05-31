/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository;

import com.paq.pojo.LearningLog;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface LearningLogRepository {

    LearningLog addLearningLog(LearningLog log);

    List<LearningLog> getLearningLogsByUsername(String username);
}
