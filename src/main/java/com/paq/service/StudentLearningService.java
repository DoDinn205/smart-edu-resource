/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service;

import com.paq.pojo.LearningLog;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface StudentLearningService {
    LearningLog startLearning(String username,int resourceId);
    LearningLog completeLearning(String username,int resourceId);
    List<LearningLog> getHistory(String username);
}
