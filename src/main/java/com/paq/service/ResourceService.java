package com.paq.service;

import com.paq.pojo.request.ReqResourceDTO;
import com.paq.pojo.response.ResResourceDTO;
import java.util.List;
import java.util.Map;

public interface ResourceService {

    List<ResResourceDTO> getResources(Map<String, String> params);

    ResResourceDTO getResourceById(int id);

    ResResourceDTO createResource(ReqResourceDTO request);

    ResResourceDTO updateResource(int id, ReqResourceDTO request);

    void deleteResource(int id);
}
