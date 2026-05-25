package com.example.portfolio_simple_spring_mvc.application.dto.profile;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.portfolio_simple_spring_mvc.application.dto.validation.annotation.Adult;
import com.example.portfolio_simple_spring_mvc.application.dto.validation.annotation.NotNull;
import com.example.portfolio_simple_spring_mvc.application.dto.validation.annotation.ValidName;
import com.example.portfolio_simple_spring_mvc.application.dto.validation.annotation.ValidNameKana;

import jakarta.validation.constraints.Size;

public class RequestDto {

    @ValidName
    private String lastName;

    @ValidName
    private String firstName;

    @ValidNameKana
    private String lastNameKana;

    @ValidNameKana
    private String firstNameKana;

    @NotNull
    private Gender gender;

    @NotNull
    @Adult
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull
    private Birthplace birthplace;

    @Size(max = 200, message = "{size200}")
    private String memo = "";

    public RequestDto() {}
    
    public RequestDto(
        String lastName,
        String firstName,
        String lastNameKana,
        String firstNameKana,
        Gender gender,
        LocalDate dateOfBirth,
        Birthplace birthplace,
        String memo) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.lastNameKana = lastNameKana;
            this.firstNameKana = firstNameKana;
            this.gender = gender;
            this.dateOfBirth = dateOfBirth;
            this.birthplace = birthplace;
            this.memo = memo;
    }
        
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof RequestDto)) return false;
        RequestDto other = (RequestDto)obj;
        return Objects.equals(lastName, other.lastName)
            && Objects.equals(firstName, other.firstName)
            && Objects.equals(lastNameKana, other.lastNameKana)
            && Objects.equals(firstNameKana, other.firstNameKana)
            && Objects.equals(gender, other.gender)
            && Objects.equals(dateOfBirth, other.dateOfBirth)
            && Objects.equals(birthplace, other.birthplace)
            && Objects.equals(memo, other.memo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, lastNameKana, firstNameKana, gender, dateOfBirth, birthplace, memo);
    }

    //getter
    public String getFirstName() { return firstName; }    
    public String getLastName() { return lastName; }    
    public String getFirstNameKana() { return firstNameKana; }    
    public String getLastNameKana() { return lastNameKana; }    
    public Gender getGender() { return gender; }    
    public LocalDate getDateOfBirth() { return dateOfBirth; }    
    public Birthplace getBirthplace() { return birthplace; }    
    public String getMemo() { return memo; }    

    //setter
    public void setLastName(String lastName) { this.lastName = lastName; }    
    public void setFirstName(String firstName) { this.firstName = firstName; }    
    public void setLastNameKana(String lastNameKana) { this.lastNameKana = lastNameKana; }
    public void setFirstNameKana(String firstNameKana) { this.firstNameKana = firstNameKana; }
    public void setGender(Gender gender) { this.gender = gender; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setBirthplace(Birthplace birthplace) { this.birthplace = birthplace; }
    public void setMemo(String memo) { this.memo = memo; }

    public static RequestDto createObjectForTest() {
        return new RequestDto(
            "山田", 
            "太郎", 
            "ヤマダ", 
            "タロウ", 
            Gender.MALE, 
            LocalDate.now().minusYears(20), 
            Birthplace.TOKYO, 
            "趣味は読書です。"
        );
    }
}
