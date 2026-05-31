package com.paq.service.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.paq.pojo.Resource;
import com.paq.pojo.ResourceTag;
import com.paq.pojo.ResourceType;
import com.paq.pojo.Subject;
import com.paq.pojo.Topic;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqResourceDTO;
import com.paq.pojo.response.ResResourceDTO;
import com.paq.repository.ResourceRepository;
import com.paq.repository.ResourceTagRepository;
import com.paq.repository.ResourceTypeRepository;
import com.paq.repository.SubjectRepository;
import com.paq.repository.TopicRepository;
import com.paq.repository.UserRepository;
import com.paq.service.PermissionService;
import com.paq.service.ResourceService;
import com.paq.utils.DTOMapper;
import com.paq.utils.constant.FormatEnum;
import com.paq.utils.error.IdInvalidException;
import com.paq.utils.error.PermissionException;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private TopicRepository topicRepo;

    @Autowired
    private ResourceTagRepository resourceTagRepo;

    @Autowired
    private ResourceTypeRepository resourceTypeRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResResourceDTO> getResources(Map<String, String> params) {
        return this.resourceRepo.getResources(params).stream()
                .map(DTOMapper::toResResourceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResResourceDTO> getLecturerResources(Map<String, String> params) {
        this.permissionService.requireLecturerOrAdmin();
        User user = this.getCurrentUser();

        if (params == null) {
            params = new HashMap<>();
        }
        params.put("uploaderId", String.valueOf(user.getId()));

        return this.resourceRepo.getResources(params).stream()
                .map(DTOMapper::toResResourceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long countLecturerResources(Map<String, String> params) {
        this.permissionService.requireLecturerOrAdmin();
        User user = this.getCurrentUser();

        if (params == null) {
            params = new HashMap<>();
        }
        params.put("uploaderId", String.valueOf(user.getId()));

        return this.resourceRepo.countResources(params);
    }

    @Override
    public ResResourceDTO getResourceById(int id) {
        Resource resource = this.resourceRepo.getResourceById(id);
        if (resource == null) {
            throw new IdInvalidException("Resource không tồn tại");
        }

        return DTOMapper.toResResourceDTO(resource, this.resourceRepo.getRelationsBySourceId(id));
    }

    @Override
    public ResResourceDTO createResource(ReqResourceDTO request) {
        this.permissionService.requireLecturerOrAdmin();

        if (this.resourceRepo.getResourceByTitle(request.getTitle()) != null) {
            throw new IllegalArgumentException("Resource title đã tồn tại");
        }

        Resource resource = new Resource();
        resource.setCreatedAt(new Date());
        resource.setIsDeleted(Boolean.FALSE);
        resource.setUploadBy(this.getCurrentUser());
        this.copyResourceFields(resource, request);

        Resource saved = this.resourceRepo.addOrUpdateResource(resource);
        this.resourceRepo.replaceRelations(saved, this.resolveRelatedResources(request.getRelatedResourceIds(), saved.getId()));

        return DTOMapper.toResResourceDTO(saved, this.resourceRepo.getRelationsBySourceId(saved.getId()));
    }

    @Override
    public ResResourceDTO updateResource(int id, ReqResourceDTO request) {
        this.permissionService.requireResourceOwnerOrAdmin(id);

        Resource resource = this.resourceRepo.getResourceById(id);
        Resource existedResource = this.resourceRepo.getResourceByTitle(request.getTitle());
        if (existedResource != null && !existedResource.getId().equals(id)) {
            throw new IllegalArgumentException("Resource title đã tồn tại");
        }

        resource.setUpdateAt(new Date());
        this.copyResourceFields(resource, request);

        Resource saved = this.resourceRepo.addOrUpdateResource(resource);
        this.resourceRepo.replaceRelations(saved, this.resolveRelatedResources(request.getRelatedResourceIds(), saved.getId()));

        return DTOMapper.toResResourceDTO(saved, this.resourceRepo.getRelationsBySourceId(saved.getId()));
    }

    @Override
    public void deleteResource(int id) {
        this.permissionService.requireResourceOwnerOrAdmin(id);
        this.resourceRepo.deleteResource(id);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new PermissionException("Bạn chưa đăng nhập");
        }

        User user = this.userRepo.getUserByUsername(auth.getName());
        if (user == null || Boolean.FALSE.equals(user.getIsActive())) {
            throw new PermissionException("Tài khoản không hợp lệ");
        }

        return user;
    }

    private void copyResourceFields(Resource resource, ReqResourceDTO request) {
        resource.setTitle(request.getTitle());
        resource.setDescription(request.getDescription());

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            try {
                byte[] fileBytes = request.getFile().getBytes();
                FormatEnum format = FormatEnum.fromFilename(request.getFile().getOriginalFilename());
                String contentType = request.getFile().getContentType();
                String resourceType = this.resolveCloudinaryResourceType(contentType);

                Map uploadParams = ObjectUtils.asMap("resource_type", resourceType);

                if ("raw".equals(resourceType)) {
                    String originalFilename = request.getFile().getOriginalFilename();
                    if (originalFilename != null && !originalFilename.isEmpty()) {
                        String safeName = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
                        uploadParams = ObjectUtils.asMap(
                            "resource_type", resourceType,
                            "public_id", safeName
                        );
                    }
                }

                Map res = this.cloudinary.uploader().upload(fileBytes, uploadParams);
                resource.setFileUrl(res.get("secure_url").toString());
                resource.setFileSize((int) request.getFile().getSize());
                resource.setFormat(format);
                resource.setPageCount(this.resolvePageCount(format, fileBytes));
            } catch (Exception ex) {
                throw new RuntimeException("Lỗi khi upload file: " + ex.getMessage());
            }
        } else if (request.getFileUrl() != null) {
            resource.setFileUrl(request.getFileUrl());
            if (request.getFileSize() != null) {
                resource.setFileSize(request.getFileSize());
            }
            resource.setFormat(request.getFormat());
            resource.setPageCount(request.getPageCount());
        }

        if (request.getThumbnailFile() != null && !request.getThumbnailFile().isEmpty()) {
            try {
                Map uploadParams = ObjectUtils.asMap("resource_type", "image");
                Map res = this.cloudinary.uploader().upload(request.getThumbnailFile().getBytes(), uploadParams);
                resource.setThumbnailUrl(res.get("secure_url").toString());
            } catch (Exception ex) {
                throw new RuntimeException("Lỗi khi upload thumbnail: " + ex.getMessage());
            }
        } else if (request.getThumbnailUrl() != null) {
            resource.setThumbnailUrl(request.getThumbnailUrl());
        }
        resource.setLevel(request.getLevel());
        resource.setSubjectSet(this.resolveSubjects(request.getSubjectIds()));
        resource.setTopicSet(this.resolveTopics(request.getTopicIds()));
        resource.setResourceTagSet(this.resolveTags(request.getTagIds()));
        resource.setResourceTypeSet(this.resolveTypes(request.getTypeIds()));
    }

    private String resolveCloudinaryResourceType(String contentType) {
        if (contentType == null) {
            return "raw";
        }
        if (contentType.startsWith("image/")) {
            return "image";
        }
        if (contentType.startsWith("video/")) {
            return "video";
        }
        return "raw";
    }

    private Integer resolvePageCount(FormatEnum format, byte[] fileBytes) throws Exception {
        if (FormatEnum.PDF.equals(format)) {
            try (PDDocument document = Loader.loadPDF(fileBytes)) {
                return document.getNumberOfPages();
            }
        }
        if (FormatEnum.PPTX.equals(format)) {
            try (XMLSlideShow slideShow = new XMLSlideShow(new ByteArrayInputStream(fileBytes))) {
                return slideShow.getSlides().size();
            }
        }
        return null;
    }

    private Set<Subject> resolveSubjects(Set<Integer> ids) {
        Set<Subject> subjects = new HashSet<>();
        if (ids == null) {
            return subjects;
        }
        for (Integer id : ids) {
            Subject subject = this.subjectRepo.getSubjectById(id);
            if (subject == null || Boolean.TRUE.equals(subject.getIsDeleted())) {
                throw new IdInvalidException("Subject không tồn tại: " + id);
            }
            subjects.add(subject);
        }
        return subjects;
    }

    private Set<Topic> resolveTopics(Set<Integer> ids) {
        Set<Topic> topics = new HashSet<>();
        if (ids == null) {
            return topics;
        }
        for (Integer id : ids) {
            Topic topic = this.topicRepo.getTopicById(id);
            if (topic == null || Boolean.TRUE.equals(topic.getIsDeleted())) {
                throw new IdInvalidException("Topic không tồn tại: " + id);
            }
            topics.add(topic);
        }
        return topics;
    }

    private Set<ResourceTag> resolveTags(Set<Integer> ids) {
        Set<ResourceTag> tags = new HashSet<>();
        if (ids == null) {
            return tags;
        }
        for (Integer id : ids) {
            ResourceTag tag = this.resourceTagRepo.getResourceTagById(id);
            if (tag == null || Boolean.TRUE.equals(tag.getIsDeleted())) {
                throw new IdInvalidException("Resource tag không tồn tại: " + id);
            }
            tags.add(tag);
        }
        return tags;
    }

    private Set<ResourceType> resolveTypes(Set<Integer> ids) {
        Set<ResourceType> types = new HashSet<>();
        if (ids == null) {
            return types;
        }
        for (Integer id : ids) {
            ResourceType type = this.resourceTypeRepo.getResourceTypeById(id);
            if (type == null || Boolean.TRUE.equals(type.getIsDeleted())) {
                throw new IdInvalidException("Resource type không tồn tại: " + id);
            }
            types.add(type);
        }
        return types;
    }

    private List<Resource> resolveRelatedResources(Set<Integer> ids, Integer sourceId) {
        List<Resource> resources = new ArrayList<>();
        if (ids == null) {
            return resources;
        }
        for (Integer id : ids) {
            if (id.equals(sourceId)) {
                throw new IllegalArgumentException("Resource không thể liên quan chính nó");
            }
            Resource resource = this.resourceRepo.getResourceById(id);
            if (resource == null) {
                throw new IdInvalidException("Resource liên quan không tồn tại: " + id);
            }
            resources.add(resource);
        }
        return resources;
    }
}
