/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository;

import com.paq.pojo.Interaction;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface InteractionRepository {

    List<Interaction> getInteractionsByResourceId(int resourceId);

    Interaction getInteractionById(int id);

    Interaction addInteraction(Interaction interaction);

    Interaction updateInteraction(Interaction interaction);
}
