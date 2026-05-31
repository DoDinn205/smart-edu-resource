package com.paq.controllers.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

import com.paq.pojo.request.ReqForumCategoryDTO;
import com.paq.pojo.response.ResForumCategoryDTO;
import com.paq.pojo.response.ResForumThreadDTO;
import com.paq.pojo.response.ResPageDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.ForumCategoryService;
import com.paq.service.ForumThreadService;
import com.paq.service.ForumPostService;
import com.paq.utils.DTOMapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/secure/admin")
public class ApiAdminForumController {

    @Autowired
    private ForumCategoryService categoryService;

    @Autowired
    private ForumThreadService threadService;

    @Autowired
    private ForumPostService postService;

    @Autowired
    private Environment env;

    @GetMapping("/forum-categories")
    public ResponseEntity<ResResponse<ResPageDTO<ResForumCategoryDTO>>> getCategories(
            @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("forum_categories.page_size", Integer.class, 10);
        long totalItems = this.categoryService.countCategories(new HashMap<>(params));

        ResResponse<ResPageDTO<ResForumCategoryDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lay danh sach danh muc dien dan thanh cong");
        res.setData(DTOMapper.toResPageDTO(this.categoryService.getCategories(params), totalItems, page, pageSize));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/forum-categories")
    public ResponseEntity<ResResponse<ResForumCategoryDTO>> createCategory(
            @Valid @RequestBody ReqForumCategoryDTO request) {
        ResResponse<ResForumCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo danh mục diễn đàn thành công");
        res.setData(this.categoryService.createCategory(request));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/forum-categories/{id}")
    public ResponseEntity<ResResponse<ResForumCategoryDTO>> updateCategory(
            @PathVariable int id,
            @Valid @RequestBody ReqForumCategoryDTO request) {
        ResResponse<ResForumCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật danh mục diễn đàn thành công");
        res.setData(this.categoryService.updateCategory(id, request));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/forum-categories/{id}")
    public ResponseEntity<ResResponse<Object>> deleteCategory(@PathVariable int id) {
        this.categoryService.deleteCategory(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa danh mục diễn đàn thành công");
        return ResponseEntity.ok(res);
    }

    @PutMapping("/forum-threads/{id}/lock")
    public ResponseEntity<ResResponse<ResForumThreadDTO>> updateThreadLock(
            @PathVariable int id,
            @NotNull(message = "isLock khong duoc de trong") @RequestParam Boolean isLock) {
        ResResponse<ResForumThreadDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật trạng thái khóa chủ đề thành công");
        res.setData(this.threadService.updateThreadLock(id, isLock));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/forum-threads/{id}")
    public ResponseEntity<ResResponse<Object>> deleteThread(@PathVariable int id) {
        this.threadService.deleteThread(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa chủ đề diễn đàn thành công");
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/forum-posts/{id}")
    public ResponseEntity<ResResponse<Object>> deletePost(@PathVariable int id) {
        this.postService.deletePost(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa bài viết vi phạm thành công");
        return ResponseEntity.ok(res);
    }
}
