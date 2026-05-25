package com.paq.controllers.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.request.ReqCategoryDTO;
import com.paq.pojo.response.ResCategoryDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.ResourceTagService;

import jakarta.validation.Valid;

@RestController
public class ApiResourceTagController {

    @Autowired
    private ResourceTagService resourceTagService;

    @GetMapping("/api/resource-tags")
    public ResponseEntity<ResResponse<List<ResCategoryDTO>>> getResourceTags(@RequestParam Map<String, String> params) {
        ResResponse<List<ResCategoryDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách thẻ tài nguyên thành công");
        res.setData(this.resourceTagService.getResourceTags(params));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/api/resource-tags/{id}")
    public ResponseEntity<ResResponse<ResCategoryDTO>> getResourceTagById(@PathVariable int id) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin thẻ tài nguyên thành công");
        res.setData(this.resourceTagService.getResourceTagById(id));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/api/secure/resource-tags")
    public ResponseEntity<ResResponse<ResCategoryDTO>> createResourceTag(@Valid @RequestBody ReqCategoryDTO request) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo thẻ tài nguyên thành công");
        res.setData(this.resourceTagService.createResourceTag(request));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/api/secure/resource-tags/{id}")
    public ResponseEntity<ResResponse<ResCategoryDTO>> updateResourceTag(@PathVariable int id,
            @Valid @RequestBody ReqCategoryDTO request) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật thẻ tài nguyên thành công");
        res.setData(this.resourceTagService.updateResourceTag(id, request));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/api/secure/resource-tags/{id}")
    public ResponseEntity<ResResponse<Object>> deleteResourceTag(@PathVariable int id) {
        this.resourceTagService.deleteResourceTag(id);
        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa thẻ tài nguyên thành công");
        return ResponseEntity.ok(res);
    }
}
