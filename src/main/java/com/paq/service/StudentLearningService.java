/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service;

import com.paq.pojo.response.ResLearningLogDTO;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface StudentLearningService {
    ResLearningLogDTO startLearning(String username,int resourceId);
    ResLearningLogDTO completeLearning(String username,int resourceId);
    List<ResLearningLogDTO> getHistory(String username);
}
