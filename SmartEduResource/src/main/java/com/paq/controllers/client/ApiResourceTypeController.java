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
import com.paq.service.ResourceTypeService;

@RestController
@RequestMapping("/api")
public class ApiResourceTypeController {

    @Autowired
    private ResourceTypeService resourceTypeService;

    @GetMapping("/resource-types")
    public ResponseEntity<ResResponse<List<ResCategoryDTO>>> getResourceTypes(
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResCategoryDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách loại tài nguyên thành công");
        res.setData(this.resourceTypeService.getResourceTypes(params));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/resource-types/{id}")
    public ResponseEntity<ResResponse<ResCategoryDTO>> getResourceTypeById(@PathVariable int id) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin loại tài nguyên thành công");
        res.setData(this.resourceTypeService.getResourceTypeById(id));
        return ResponseEntity.ok(res);
    }
}
