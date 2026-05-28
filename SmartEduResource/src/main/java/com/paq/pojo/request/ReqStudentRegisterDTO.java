package com.paq.pojo.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.paq.utils.constant.EduLevelEnum;
import com.paq.utils.constant.ExpLevelEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ReqStudentRegisterDTO {

    @NotBlank(message = "Ho tên không được để trống")
    @Size(max = 100, message = "Ho tên tối đa 100 ký tự")
    private String fullName;

    @NotBlank(message = "Username không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username chỉ được gồm chữ cái, số và dấu gạch dưới")
    @Size(max = 100, message = "Username tối đa 100 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email tối đa 100 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự")
    private String password;

    @Size(max = 20, message = "Số điện thoại tối đa 20 ký tự")
    private String phone;

    @Size(max = 50, message = "Mã sinh viên tối đa 50 ký tự")
    private String studentCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    private Integer gender;
    private ExpLevelEnum experienceLevel;
    private EduLevelEnum educationLevel;
    private String learningGoal;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public ExpLevelEnum getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(ExpLevelEnum experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public EduLevelEnum getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(EduLevelEnum educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getLearningGoal() {
        return learningGoal;
    }

    public void setLearningGoal(String learningGoal) {
        this.learningGoal = learningGoal;
    }
}
