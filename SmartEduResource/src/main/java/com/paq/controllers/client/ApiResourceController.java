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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.request.ReqResourceDTO;
import com.paq.pojo.response.ResResourceDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.ResourceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/resources")
    public ResponseEntity<ResResponse<List<ResResourceDTO>>> getResources(@RequestParam Map<String, String> params) {
        ResResponse<List<ResResourceDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách học liệu thành công");
        res.setData(this.resourceService.getResources(params));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/resources/{id}")
    public ResponseEntity<ResResponse<ResResourceDTO>> getResourceById(@PathVariable int id) {
        ResResponse<ResResourceDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin học liệu thành công");
        res.setData(this.resourceService.getResourceById(id));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/secure/resources")
    public ResponseEntity<ResResponse<ResResourceDTO>> createResource(@Valid @RequestBody ReqResourceDTO request) {
        ResResponse<ResResourceDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo học liệu thành công");
        res.setData(this.resourceService.createResource(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/secure/resources/{id}")
    public ResponseEntity<ResResponse<ResResourceDTO>> updateResource(@PathVariable int id,
            @Valid @RequestBody ReqResourceDTO request) {
        ResResponse<ResResourceDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật học liệu thành công");
        res.setData(this.resourceService.updateResource(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/secure/resources/{id}")
    public ResponseEntity<ResResponse<Object>> deleteResource(@PathVariable int id) {
        this.resourceService.deleteResource(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa học liệu thành công");

        return ResponseEntity.ok(res);
    }
}
