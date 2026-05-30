package com.paq.controllers.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.request.ReqResourceDTO;
import com.paq.pojo.response.ResResourceDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.ResourceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/secure/lecturer")
public class ApiLecturerResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/resources")
    public ResponseEntity<ResResponse<ResResourceDTO>> createResource(@Valid @RequestBody ReqResourceDTO request) {
        ResResponse<ResResourceDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo học liệu thành công");
        res.setData(this.resourceService.createResource(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/resources/{id}")
    public ResponseEntity<ResResponse<ResResourceDTO>> updateResource(@PathVariable int id,
            @Valid @RequestBody ReqResourceDTO request) {
        ResResponse<ResResourceDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật học liệu thành công");
        res.setData(this.resourceService.updateResource(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/resources/{id}")
    public ResponseEntity<ResResponse<Object>> deleteResource(@PathVariable int id) {
        this.resourceService.deleteResource(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa học liệu thành công");

        return ResponseEntity.ok(res);
    }
}
