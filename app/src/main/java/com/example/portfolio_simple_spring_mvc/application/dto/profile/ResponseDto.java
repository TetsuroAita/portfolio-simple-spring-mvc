package com.example.portfolio_simple_spring_mvc.application.dto.profile;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.example.portfolio_simple_spring_mvc.application.util.DateTimeUtil;

public class ResponseDto {
    
    private UUID id;
    
    private String createdAt;
    
    private String updatedAt;

    private boolean status;

    private String personalNumber;

    private String fullName;

    private String fullNameKana;
    
    private String gender;

    private String dateOfBirth;

    private int age;

    private String birthplace;

    private String memo;

    private boolean avatar;

    public ResponseDto() {}

    public ResponseDto(UUID id, String createdAt, String updatedAt, boolean status, String personalNumber, String fullName, String fullNameKana,
            String gender, String dateOfBirth, int age, String birthplace, String memo, boolean avatar) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.personalNumber = personalNumber;
        this.fullName = fullName;
        this.fullNameKana = fullNameKana;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.birthplace = birthplace;
        this.memo = memo;
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof ResponseDto)) return false;
        ResponseDto other = (ResponseDto)obj;
        return Objects.equals(id, other.id)
            && Objects.equals(createdAt, other.createdAt)
            && Objects.equals(updatedAt, other.updatedAt)
            && Objects.equals(status, other.status)
            && Objects.equals(personalNumber, other.personalNumber)
            && Objects.equals(fullName, other.fullName)
            && Objects.equals(fullNameKana, other.fullNameKana)
            && Objects.equals(gender, other.gender)
            && Objects.equals(dateOfBirth, other.dateOfBirth)
            && Objects.equals(age, other.age)
            && Objects.equals(birthplace, other.birthplace)
            && Objects.equals(memo, other.memo)
            && Objects.equals(avatar, other.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, status, personalNumber, fullName, fullNameKana, gender, dateOfBirth, age, birthplace, memo, avatar);
    }

    //getter
    public UUID getId() { return id; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public boolean getStatus() { return status; }
    public String getPersonalNumber() { return personalNumber; }
    public String getFullName() { return fullName; }
    public String getFullNameKana() { return fullNameKana; }
    public String getGender() { return gender; }
    public String getDateOfBirth() { return dateOfBirth; }
    public int getAge() { return age; }
    public String getBirthplace() { return birthplace; }
    public String getMemo() { return memo; }
    public boolean getAvatar() { return avatar; }

    //setter
    public void setId(UUID id) { this.id = id; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) {  this.updatedAt = updatedAt; }
    public void setStatus(boolean status) {  this.status = status; }
    public void setPersonalNumber(String personalNumber) { this.personalNumber = personalNumber; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setFullNameKana(String fullNameKana) { this.fullNameKana = fullNameKana; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setAge(int age) { this.age = age; }
    public void setBirthplace(String birthplace) { this.birthplace = birthplace; }
    public void setMemo(String memo) { this.memo = memo; }
    public void setAvatar(boolean avatar) { this.avatar = avatar; }

    public static ResponseDto createObjectForTest() {
        return new ResponseDto(
            null, 
            null, 
            null, 
            true,
            "001", 
            "山田 太郎",
            "ヤマダ タロウ", 
            Gender.MALE.getDisplayName(), 
            DateTimeUtil.formatDate(LocalDate.now().minusYears(20)),
            20,
            Birthplace.TOKYO.getDisplayName(),
            "趣味は読書です。",
            false
        );
    }
}
