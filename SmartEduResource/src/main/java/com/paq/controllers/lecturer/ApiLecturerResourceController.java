package com.paq.controllers.lecturer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.request.ReqResourceDTO;
import com.paq.pojo.response.ResPageDTO;
import com.paq.pojo.response.ResResourceDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.ResourceService;
import com.paq.utils.DTOMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/secure/lecturer")
public class ApiLecturerResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private Environment env;

    @GetMapping("/resources")
    public ResponseEntity<ResResponse<ResPageDTO<ResResourceDTO>>> getLecturerResources(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("resources.page_size", Integer.class);

        Map<String, String> countParams = new HashMap<>(params);
        Long totalItems = this.resourceService.countLecturerResources(countParams);

        ResPageDTO<ResResourceDTO> pageDTO = DTOMapper.toResPageDTO(this.resourceService.getLecturerResources(params), totalItems, page, pageSize);

        ResResponse<ResPageDTO<ResResourceDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách học liệu của giảng viên thành công");
        res.setData(pageDTO);

        return ResponseEntity.ok(res);
    }

    @PostMapping(path = "/resources", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResResponse<ResResourceDTO>> createResource(@Valid @ModelAttribute ReqResourceDTO request) {
        ResResponse<ResResourceDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo học liệu thành công");
        res.setData(this.resourceService.createResource(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping(path = "/resources/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResResponse<ResResourceDTO>> updateResource(@PathVariable("id") int id,
            @Valid @ModelAttribute ReqResourceDTO request) {
        ResResponse<ResResourceDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật học liệu thành công");
        res.setData(this.resourceService.updateResource(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/resources/{id}")
    public ResponseEntity<ResResponse<Object>> deleteResource(@PathVariable("id") int id) {
        this.resourceService.deleteResource(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa học liệu thành công");

        return ResponseEntity.ok(res);
    }
}
