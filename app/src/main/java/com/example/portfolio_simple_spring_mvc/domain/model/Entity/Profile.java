package com.example.portfolio_simple_spring_mvc.domain.model.Entity;

import java.time.LocalDate;
import java.util.Objects;

import com.example.portfolio_simple_spring_mvc.domain.model.value.FullName;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "profile")
public class Profile extends BaseEntityModel {

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "personal_number", unique = true, nullable = false, length = 3)
    private String personalNumber;

    @Embedded
    private FullName fullName;

    @Column(name = "gender", nullable = false, length = 6)
    private String gender;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "birthplace", nullable = false, length = 9)
    private String birthplace;

    @Column(name = "memo", nullable = false, length = 200)
    private String memo = "";

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "avatar_id", nullable = true, unique = true)
    private Avatar avatar = null;
    
    //リフレクション用
    @Deprecated
    protected Profile() {}

    //新規作成用
    public Profile(String personalNumber, FullName fullName, String gender, LocalDate dateOfBirth, String birthplace, String memo) {
        super();
        this.personalNumber = Objects.requireNonNull(personalNumber);
        this.fullName = Objects.requireNonNull(fullName);
        this.gender = Objects.requireNonNull(gender);
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth);
        this.birthplace = Objects.requireNonNull(birthplace);
        this.memo = Objects.requireNonNull(memo);
    }
    
    //getter
    public boolean activeStatus() { return active; }
    public String getPersonalNumber() { return this.personalNumber; }
    public FullName getFullName() { return this.fullName; }    
    public String getGender() { return this.gender; }    
    public LocalDate getDateOfBirth() { return this.dateOfBirth; }    
    public String getBirthplace() { return this.birthplace; }    
    public String getMemo() { return this.memo; }
    public Avatar getAvatar() { return this.avatar; }

    //setter(更新用、更新可能なモノのみ)
    public void setFullName(FullName fullName) { this.fullName = Objects.requireNonNull(fullName); }    
    public void setGender(String gender) { this.gender = Objects.requireNonNull(gender); }    
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = Objects.requireNonNull(dateOfBirth); }    
    public void setBirthplace(String birthplace) { this.birthplace = Objects.requireNonNull(birthplace); }    
    public void setMemo(String memo) { this.memo = Objects.requireNonNull(memo); }
    public void setAvatar(Avatar avatar) { this.avatar = avatar; }

    public Profile inactivate() {
        this.active = false;
        return this;
    }

    public Profile activate() {
        this.active = true;
        return this;
    }

    public static Profile createObjectForTest() {
        return new Profile(
            "001", 
            FullName.createObjectForTest(), 
            "MALE", 
            LocalDate.now().minusYears(20), 
            "TOKYO", 
            "趣味は読書です。"
        );
    }
}
