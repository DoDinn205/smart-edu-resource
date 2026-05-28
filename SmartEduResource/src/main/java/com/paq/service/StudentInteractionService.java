/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service;

import com.paq.pojo.request.ReqInteractionDTO;
import com.paq.pojo.request.ReqInteractionReplyDTO;
import com.paq.pojo.response.ResInteractionDTO;
import com.paq.pojo.response.ResInteractionReplyDTO;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface StudentInteractionService {
    List<ResInteractionDTO> getInteractionsByResourceId(int resourceId);

    ResInteractionDTO createInteraction(String username, int resourceId, ReqInteractionDTO request);

    ResInteractionDTO updateInteraction(String username, int interactionId, ReqInteractionDTO request);

    ResInteractionDTO deleteInteraction(String username, int interactionId);

    List<ResInteractionReplyDTO> getRepliesByInteractionId(int interactionId);

    ResInteractionReplyDTO createReply(String username, int interactionId, ReqInteractionReplyDTO request);

    ResInteractionReplyDTO updateReply(String username, int replyId, ReqInteractionReplyDTO request);

    ResInteractionReplyDTO deleteReply(String username, int replyId);
}
