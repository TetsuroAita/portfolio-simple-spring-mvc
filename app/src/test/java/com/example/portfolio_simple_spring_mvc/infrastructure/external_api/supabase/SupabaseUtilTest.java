package com.example.portfolio_simple_spring_mvc.infrastructure.external_api.supabase;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.infrastructure.external_api.dto.ApiRequest.Supabase_ExpiresObjectURL;

public class SupabaseUtilTest {
    private SupabaseUtil supabaseUtil;

    @BeforeEach
    void setUp() {
        supabaseUtil = new SupabaseUtil(
            "https://sample.com",
            "abc",
            "test_bucket",
            1L
        );
    }

    @Test
    @DisplayName("avatarURL を生成")
    void test_generateAvatarURL() {
        String expected = "https://sample.com/storage/v1/object/sign/test_bucket/test.png?token=123";
        String result = supabaseUtil.generateAvatarURL("test.png", "123");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("取得した presignedURl からトークンのみ取り出す")
    void test_pickOutToken() {
        String expected = "123";
        String result = supabaseUtil.pickOutToken("?token=123");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("認証キー生成")
    void test_apiKey() {
        String expected = "Bearer abc";
        String result = supabaseUtil.apiKey();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("有効期間設定値をセットするオブジェクトを生成")
    void test_expiresIn() {
        Supabase_ExpiresObjectURL expected = new Supabase_ExpiresObjectURL(1L);
        Supabase_ExpiresObjectURL result = supabaseUtil.expiresIn();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("path を渡すとURLを生成")
    void test_genarateURLtoRetrieveAnObjectURI() {
        String expected = "https://sample.com/storage/v1/object/sign/test_bucket/test.png";
        String result = supabaseUtil.genarateURLtoRetrieveAnObjectURI("test.png");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("path を渡すとURLを生成")
    void test_uploadAnObjectURI() {
        String expected = "https://sample.com/storage/v1/object/test_bucket/test.png";
        String result = supabaseUtil.uploadAnObjectURI("test.png");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("path を渡すとURLを生成")
    void test_deleteMultipleObjectsURI() {
        String expected = "https://sample.com/storage/v1/object/test_bucket";
        String result = supabaseUtil.deleteMultipleObjectsURI();
        assertThat(result).isEqualTo(expected);
    }
}
