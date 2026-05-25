package com.example.portfolio_simple_spring_mvc.domain.model.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;

public class AvatarTest {

    @Test
    void generatePath_Test() {
        Avatar avatar = Avatar.createObjectForTest();
        String changedStem = avatar.getChangedStem();
        String expected = changedStem + ".png";

        String result = avatar.generatePath();
        
        System.out.println("expected: " + expected);
        System.out.println("result  : " + result);

        assertThat(result).isEqualTo(expected);
    }
}
