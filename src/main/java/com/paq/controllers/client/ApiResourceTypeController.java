package com.paq.controllers.client;

import com.paq.pojo.request.ReqCategoryDTO;
import com.paq.pojo.response.ResCategoryDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.ResourceTypeService;
import jakarta.validation.Valid;
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

@RestController
public class ApiResourceTypeController {

    @Autowired
    private ResourceTypeService resourceTypeService;

    @GetMapping("/api/resource-types")
    public ResponseEntity<ResResponse<List<ResCategoryDTO>>> getResourceTypes(@RequestParam Map<String, String> params) {
        ResResponse<List<ResCategoryDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Get resource types successfully");
        res.setData(this.resourceTypeService.getResourceTypes(params));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/api/resource-types/{id}")
    public ResponseEntity<ResResponse<ResCategoryDTO>> getResourceTypeById(@PathVariable int id) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Get resource type successfully");
        res.setData(this.resourceTypeService.getResourceTypeById(id));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/api/secure/resource-types")
    public ResponseEntity<ResResponse<ResCategoryDTO>> createResourceType(@Valid @RequestBody ReqCategoryDTO request) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Create resource type successfully");
        res.setData(this.resourceTypeService.createResourceType(request));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/api/secure/resource-types/{id}")
    public ResponseEntity<ResResponse<ResCategoryDTO>> updateResourceType(@PathVariable int id,
            @Valid @RequestBody ReqCategoryDTO request) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Update resource type successfully");
        res.setData(this.resourceTypeService.updateResourceType(id, request));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/api/secure/resource-types/{id}")
    public ResponseEntity<ResResponse<Object>> deleteResourceType(@PathVariable int id) {
        this.resourceTypeService.deleteResourceType(id);
        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delete resource type successfully");
        return ResponseEntity.ok(res);
    }
}
