package com.paq.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.paq.pojo.Lecturer;
import com.paq.pojo.Student;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqLecturerApprovalDTO;
import com.paq.pojo.request.ReqLecturerDTO;
import com.paq.pojo.request.ReqRegisterDTO;
import com.paq.pojo.request.ReqStudentDTO;
import com.paq.pojo.request.ReqStudentRegisterDTO;
import com.paq.pojo.request.ReqUserStatusDTO;
import com.paq.pojo.response.ResLecturerDTO;
import com.paq.pojo.response.ResStudentDTO;
import com.paq.pojo.response.ResUserDTO;
import com.paq.repository.UserRepository;
import com.paq.service.PermissionService;
import com.paq.service.UserService;
import com.paq.utils.DTOMapper;
import com.paq.utils.constant.RoleEnum;
import com.paq.utils.error.IdInvalidException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service("userDetailsService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private Validator validator;

    @Override
    public List<ResUserDTO> getUsers(Map<String, String> params) {
        this.permissionService.requireAdmin();
        return this.userRepo.getUsers(params).stream()
                .map(DTOMapper::toResUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long countUsers(Map<String, String> params) {
        this.permissionService.requireAdmin();
        return this.userRepo.countUsers(params);
    }

    @Override
    public ResUserDTO getUserById(int id) {
        this.permissionService.requireAdmin();
        User user = this.userRepo.getUserById(id);
        if (user == null) {
            throw new IdInvalidException("User không tồn tại");
        }

        return DTOMapper.toResUserDTO(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepo.getUserByUsername(username);
    }

    @Override
    public User addUser(ReqRegisterDTO req) {
        if (this.userRepo.getUserByUsername(req.getUsername()) != null) {
            throw new IllegalArgumentException("Username đã tồn tại!");
        }
        if (this.userRepo.getUserByEmail(req.getEmail()) != null) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }

        User u = new User();
        u.setFullName(req.getFullName());
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPhone(req.getPhone());
        u.setPassword(this.passwordEncoder.encode(req.getPassword()));
        u.setRole(RoleEnum.STUDENT);
        u.setIsActive(true);
        u.setCreatedAt(new Date());

        MultipartFile avatar = req.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(avatar.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.userRepo.addUser(u);
    }

    @Override
    public ResStudentDTO registerStudent(ReqStudentRegisterDTO request) {
        this.validateUniqueUser(request.getUsername(), request.getEmail(), null);

        User user = this.buildBaseUser(request.getFullName(), request.getUsername(), request.getEmail(),
                request.getPhone(), request.getPassword(), RoleEnum.STUDENT);
        this.userRepo.addUser(user);

        Student student = new Student();
        student.setUserId(user);
        student.setStudentCode(request.getStudentCode());
        student.setDob(request.getDob());
        student.setGender(request.getGender());
        student.setExperienceLevel(request.getExperienceLevel());
        student.setEducationLevel(request.getEducationLevel());
        student.setLearningGoal(request.getLearningGoal());

        return DTOMapper.toResStudentDTO(this.userRepo.addOrUpdateStudent(student));
    }

    @Override
    @Transactional
    public ResLecturerDTO registerLecturer(ReqLecturerDTO request) {
        this.validateLecturerRequest(request);
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Mật khẩu là bắt buộc khi đăng ký giảng viên");
        }
        this.validateUniqueUser(request.getUsername(), request.getEmail(), null);
        String certificateUrl = this.uploadLecturerCertificate(request.getCertificate());

        User user = this.buildBaseUser(request.getFullName(), request.getUsername(), request.getEmail(),
                request.getPhone(), request.getPassword(), RoleEnum.LECTURER);
        this.userRepo.addUser(user);

        Lecturer lecturer = new Lecturer();
        lecturer.setUserId(user);
        lecturer.setDegree(request.getDegree());
        lecturer.setCertificateUrl(certificateUrl);
        lecturer.setSpecialization(request.getSpecialization());
        lecturer.setBio(request.getBio());
        lecturer.setIsApprove(Boolean.FALSE);
        lecturer.setApproveAt(null);

        return DTOMapper.toResLecturerDTO(this.userRepo.addOrUpdateLecturer(lecturer));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Không tồn tại!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }

    @Override
    public boolean authenticate(String username, String password) {
        User user = this.userRepo.getUserByUsername(username);
        return user != null
                && !Boolean.FALSE.equals(user.getIsActive())
                && this.passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public ResUserDTO updateUserStatus(int id, ReqUserStatusDTO request) {
        this.permissionService.requireAdmin();
        User user = this.getExistingUser(id);
        user.setIsActive(request.getIsActive());

        return DTOMapper.toResUserDTO(this.userRepo.updateUser(user));
    }

    @Override
    public List<ResStudentDTO> getStudents(Map<String, String> params) {
        this.permissionService.requireAdmin();
        return this.userRepo.getStudents(params).stream()
                .map(DTOMapper::toResStudentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long countStudents(Map<String, String> params) {
        this.permissionService.requireAdmin();
        return this.userRepo.countStudents(params);
    }

    @Override
    public ResStudentDTO getStudentById(int id) {
        this.permissionService.requireAdmin();
        Student student = this.getExistingStudent(id);
        return DTOMapper.toResStudentDTO(student);
    }

    @Override
    public ResStudentDTO createStudent(ReqStudentDTO request) {
        this.permissionService.requireAdmin();
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Mật khẩu là bắt buộc khi tạo sinh viên");
        }

        this.validateUniqueUser(request.getUsername(), request.getEmail(), null);

        User user = this.buildBaseUser(request.getFullName(), request.getUsername(), request.getEmail(),
                request.getPhone(), request.getPassword(), RoleEnum.STUDENT);
        this.userRepo.addUser(user);

        Student student = new Student();
        student.setUserId(user);
        this.applyStudentFields(student, request);

        return DTOMapper.toResStudentDTO(this.userRepo.addOrUpdateStudent(student));
    }

    @Override
    public ResStudentDTO updateStudent(int id, ReqStudentDTO request) {
        this.permissionService.requireAdmin();
        Student student = this.getExistingStudent(id);
        User user = student.getUserId();
        this.validateUniqueUser(request.getUsername(), request.getEmail(), user.getId());
        this.applyUserFields(user, request.getFullName(), request.getUsername(), request.getEmail(),
                request.getPhone(), request.getPassword());
        this.userRepo.updateUser(user);

        this.applyStudentFields(student, request);

        return DTOMapper.toResStudentDTO(this.userRepo.addOrUpdateStudent(student));
    }

    @Override
    public void deleteStudent(int id) {
        this.permissionService.requireAdmin();
        Student student = this.getExistingStudent(id);
        User user = student.getUserId();
        user.setIsActive(Boolean.FALSE);
        this.userRepo.updateUser(user);
    }

    @Override
    public List<ResLecturerDTO> getLecturers(Map<String, String> params) {
        this.permissionService.requireAdmin();
        return this.userRepo.getLecturers(params).stream()
                .map(DTOMapper::toResLecturerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long countLecturers(Map<String, String> params) {
        this.permissionService.requireAdmin();
        return this.userRepo.countLecturers(params);
    }

    @Override
    public ResLecturerDTO getLecturerById(int id) {
        this.permissionService.requireAdmin();
        Lecturer lecturer = this.getExistingLecturer(id);
        return DTOMapper.toResLecturerDTO(lecturer);
    }

    @Override
    public ResLecturerDTO createLecturer(ReqLecturerDTO request) {
        this.permissionService.requireAdmin();
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Mat khau la bat buoc khi tao giang vien");
        }

        this.validateUniqueUser(request.getUsername(), request.getEmail(), null);

        User user = this.buildBaseUser(request.getFullName(), request.getUsername(), request.getEmail(),
                request.getPhone(), request.getPassword(), RoleEnum.LECTURER);
        this.userRepo.addUser(user);

        Lecturer lecturer = new Lecturer();
        lecturer.setUserId(user);
        this.applyLecturerFields(lecturer, request);

        return DTOMapper.toResLecturerDTO(this.userRepo.addOrUpdateLecturer(lecturer));
    }

    @Override
    public ResLecturerDTO updateLecturer(int id, ReqLecturerDTO request) {
        this.permissionService.requireAdmin();
        Lecturer lecturer = this.getExistingLecturer(id);
        User user = lecturer.getUserId();
        this.validateUniqueUser(request.getUsername(), request.getEmail(), user.getId());
        this.applyUserFields(user, request.getFullName(), request.getUsername(), request.getEmail(),
                request.getPhone(), request.getPassword());
        this.userRepo.updateUser(user);

        this.applyLecturerFields(lecturer, request);

        return DTOMapper.toResLecturerDTO(this.userRepo.addOrUpdateLecturer(lecturer));
    }

    @Override
    public ResLecturerDTO updateLecturerApproval(int id, ReqLecturerApprovalDTO request) {
        this.permissionService.requireAdmin();
        Lecturer lecturer = this.getExistingLecturer(id);
        lecturer.setIsApprove(request.getIsApprove());
        lecturer.setApproveAt(Boolean.TRUE.equals(request.getIsApprove()) ? new Date() : null);

        return DTOMapper.toResLecturerDTO(this.userRepo.addOrUpdateLecturer(lecturer));
    }

    @Override
    public void deleteLecturer(int id) {
        this.permissionService.requireAdmin();
        Lecturer lecturer = this.getExistingLecturer(id);
        User user = lecturer.getUserId();
        user.setIsActive(Boolean.FALSE);
        this.userRepo.updateUser(user);
    }

    private User buildBaseUser(String fullName, String username, String email, String phone,
            String password, RoleEnum role) {
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setRole(role);
        user.setIsActive(Boolean.TRUE);
        user.setCreatedAt(new Date());

        return user;
    }

    private void applyUserFields(User user, String fullName, String username, String email,
            String phone, String password) {
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        if (password != null && !password.isBlank()) {
            user.setPassword(this.passwordEncoder.encode(password));
        }
    }

    private void applyStudentFields(Student student, ReqStudentDTO request) {
        student.setStudentCode(request.getStudentCode());
        student.setDob(request.getDob());
        student.setGender(request.getGender());
        student.setExperienceLevel(request.getExperienceLevel());
        student.setEducationLevel(request.getEducationLevel());
        student.setLearningGoal(request.getLearningGoal());
    }

    private void applyLecturerFields(Lecturer lecturer, ReqLecturerDTO request) {
        lecturer.setDegree(request.getDegree());
        if (request.getCertificate() != null && !request.getCertificate().isEmpty()) {
            lecturer.setCertificateUrl(this.uploadLecturerCertificate(request.getCertificate()));
        } else if (request.getCertificateUrl() != null && !request.getCertificateUrl().isBlank()) {
            lecturer.setCertificateUrl(request.getCertificateUrl());
        }
        lecturer.setSpecialization(request.getSpecialization());
        lecturer.setBio(request.getBio());
        if (request.getIsApprove() != null) {
            lecturer.setIsApprove(request.getIsApprove());
            lecturer.setApproveAt(Boolean.TRUE.equals(request.getIsApprove()) ? new Date() : null);
        } else if (lecturer.getId() == null) {
            lecturer.setIsApprove(Boolean.FALSE);
        }
    }

    private String uploadLecturerCertificate(MultipartFile certificate) {
        this.validateLecturerCertificate(certificate, true);

        try {
            Map res = this.cloudinary.uploader().upload(certificate.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "raw",
                            "folder", "lecturer-certificates"));
            return res.get("secure_url").toString();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Không thể upload chứng chỉ: " + ex.getMessage());
        }
    }

    private void validateLecturerCertificate(MultipartFile certificate, boolean required) {
        if (certificate == null || certificate.isEmpty()) {
            if (required) {
                throw new IllegalArgumentException("Chứng chỉ PDF là bắt buộc");
            }
            return;
        }

        if (!"application/pdf".equalsIgnoreCase(certificate.getContentType())) {
            throw new IllegalArgumentException("Chứng chỉ phải là file PDF");
        }
        if (certificate.getSize() > 5L * 1024L * 1024L) {
            throw new IllegalArgumentException("Chứng chỉ tối đa 5 MB");
        }
    }

    private void validateLecturerRequest(ReqLecturerDTO request) {
        Set<ConstraintViolation<ReqLecturerDTO>> violations = this.validator.validate(request);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(" | "));
            throw new IllegalArgumentException(message);
        }
    }

    private void validateUniqueUser(String username, String email, Integer currentUserId) {
        User usernameOwner = this.userRepo.getUserByUsername(username);
        if (usernameOwner != null && !usernameOwner.getId().equals(currentUserId)) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }

        User emailOwner = this.userRepo.getUserByEmail(email);
        if (emailOwner != null && !emailOwner.getId().equals(currentUserId)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
    }

    private User getExistingUser(int id) {
        User user = this.userRepo.getUserById(id);
        if (user == null) {
            throw new IdInvalidException("User không tồn tại");
        }

        return user;
    }

    private Student getExistingStudent(int id) {
        Student student = this.userRepo.getStudentById(id);
        if (student == null) {
            throw new IdInvalidException("Student không tồn tại");
        }

        return student;
    }

    private Lecturer getExistingLecturer(int id) {
        Lecturer lecturer = this.userRepo.getLecturerById(id);
        if (lecturer == null) {
            throw new IdInvalidException("Lecturer không tồn tại");
        }

        return lecturer;
    }
}
