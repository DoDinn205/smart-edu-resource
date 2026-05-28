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

import com.paq.pojo.request.ReqForumCategoryDTO;
import com.paq.pojo.request.ReqForumPostDTO;
import com.paq.pojo.request.ReqForumThreadDTO;
import com.paq.pojo.response.ResForumCategoryDTO;
import com.paq.pojo.response.ResForumPostDTO;
import com.paq.pojo.response.ResForumThreadDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.ForumCategoryService;
import com.paq.service.ForumPostService;
import com.paq.service.ForumThreadService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class ApiForumController {

    @Autowired
    private ForumCategoryService categoryService;

    @Autowired
    private ForumThreadService threadService;

    @Autowired
    private ForumPostService postService;

    @GetMapping("/forum-categories")
    public ResponseEntity<ResResponse<List<ResForumCategoryDTO>>> getCategories(
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResForumCategoryDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách danh mục diễn đàn thành công");
        res.setData(this.categoryService.getCategories(params));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/forum-categories/{id}")
    public ResponseEntity<ResResponse<ResForumCategoryDTO>> getCategoryById(@PathVariable int id) {
        ResResponse<ResForumCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin danh mục diễn đàn thành công");
        res.setData(this.categoryService.getCategoryById(id));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/secure/forum-categories")
    public ResponseEntity<ResResponse<ResForumCategoryDTO>> createCategory(
            @Valid @RequestBody ReqForumCategoryDTO request) {
        ResResponse<ResForumCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo danh mục diễn đàn thành công");
        res.setData(this.categoryService.createCategory(request));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/secure/forum-categories/{id}")
    public ResponseEntity<ResResponse<ResForumCategoryDTO>> updateCategory(
            @PathVariable int id,
            @Valid @RequestBody ReqForumCategoryDTO request) {
        ResResponse<ResForumCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật danh mục diễn đàn thành công");
        res.setData(this.categoryService.updateCategory(id, request));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/secure/forum-categories/{id}")
    public ResponseEntity<ResResponse<Object>> deleteCategory(@PathVariable int id) {
        this.categoryService.deleteCategory(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa danh mục diễn đàn thành công");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/forum-threads")
    public ResponseEntity<ResResponse<List<ResForumThreadDTO>>> getThreads(
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResForumThreadDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách chủ đề diễn đàn thành công");
        res.setData(this.threadService.getThreads(params));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/forum-threads/{id}")
    public ResponseEntity<ResResponse<ResForumThreadDTO>> getThreadById(@PathVariable int id) {
        ResResponse<ResForumThreadDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin chủ đề diễn đàn thành công");
        res.setData(this.threadService.getThreadById(id));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/secure/forum-threads")
    public ResponseEntity<ResResponse<ResForumThreadDTO>> createThread(
            @Valid @RequestBody ReqForumThreadDTO request) {
        ResResponse<ResForumThreadDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo chủ đề diễn đàn thành công");
        res.setData(this.threadService.createThread(request));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/secure/forum-threads/{id}")
    public ResponseEntity<ResResponse<ResForumThreadDTO>> updateThread(
            @PathVariable int id,
            @Valid @RequestBody ReqForumThreadDTO request) {
        ResResponse<ResForumThreadDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật chủ đề diễn đàn thành công");
        res.setData(this.threadService.updateThread(id, request));
        return ResponseEntity.ok(res);
    }

    @PutMapping("/secure/forum-threads/{id}/lock")
    public ResponseEntity<ResResponse<ResForumThreadDTO>> updateThreadLock(
            @PathVariable int id,
            @NotNull(message = "isLock khong duoc de trong") @RequestParam Boolean isLock) {
        ResResponse<ResForumThreadDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật trạng thái khóa chủ đề thành công");
        res.setData(this.threadService.updateThreadLock(id, isLock));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/secure/forum-threads/{id}")
    public ResponseEntity<ResResponse<Object>> deleteThread(@PathVariable int id) {
        this.threadService.deleteThread(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa chủ đề diễn đàn thành công");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/forum-threads/{threadId}/posts")
    public ResponseEntity<ResResponse<List<ResForumPostDTO>>> getPostsByThreadId(
            @PathVariable int threadId,
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResForumPostDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy nội dung thảo luận thành công");
        res.setData(this.postService.getPostsByThreadId(threadId, params));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/forum-posts/{id}")
    public ResponseEntity<ResResponse<ResForumPostDTO>> getPostById(@PathVariable int id) {
        ResResponse<ResForumPostDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin bài viết diễn đàn thành công");
        res.setData(this.postService.getPostById(id));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/secure/forum-threads/{threadId}/posts")
    public ResponseEntity<ResResponse<ResForumPostDTO>> createPost(
            @PathVariable int threadId,
            @Valid @RequestBody ReqForumPostDTO request) {
        ResResponse<ResForumPostDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo bài viết diễn đàn thành công");
        res.setData(this.postService.createPost(threadId, request));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/secure/forum-posts/{id}")
    public ResponseEntity<ResResponse<ResForumPostDTO>> updatePost(
            @PathVariable int id,
            @Valid @RequestBody ReqForumPostDTO request) {
        ResResponse<ResForumPostDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật bài viết diễn đàn thành công");
        res.setData(this.postService.updatePost(id, request));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/secure/forum-posts/{id}")
    public ResponseEntity<ResResponse<Object>> deletePost(@PathVariable int id) {
        this.postService.deletePost(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa bài viết diễn đàn thành công");
        return ResponseEntity.ok(res);
    }
}
