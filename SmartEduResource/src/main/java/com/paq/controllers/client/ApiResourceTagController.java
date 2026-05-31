package com.paq.controllers.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.response.ResCategoryDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.ResourceTagService;

@RestController
@RequestMapping("/api")
public class ApiResourceTagController {

    @Autowired
    private ResourceTagService resourceTagService;

    @GetMapping("/resource-tags")
    public ResponseEntity<ResResponse<List<ResCategoryDTO>>> getResourceTags(
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResCategoryDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách thẻ tài nguyên thành công");
        res.setData(this.resourceTagService.getResourceTags(params));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/resource-tags/{id}")
    public ResponseEntity<ResResponse<ResCategoryDTO>> getResourceTagById(@PathVariable int id) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin thẻ tài nguyên thành công");
        res.setData(this.resourceTagService.getResourceTagById(id));
        return ResponseEntity.ok(res);
    }
}
