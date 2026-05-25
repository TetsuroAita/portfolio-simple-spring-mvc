package com.example.portfolio_simple_spring_mvc.infrastructure.external_api.supabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSource;
import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSourceFactory;
import com.example.portfolio_simple_spring_mvc.infrastructure.exception.StorageUnavailableException;
import com.example.portfolio_simple_spring_mvc.infrastructure.exception.SupabaseStorageException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class SupabaseStorageClientTest {
    
    private MockWebServer mockWebServer;
    private String baseUrl;
    private SupabaseUtil supabaseUtil;
    private AvatarStorageClientOfSupabase client;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        baseUrl =mockWebServer.url("").toString();

        RestClient restClient = RestClient.builder()
            .baseUrl(baseUrl)    
            .build();

        supabaseUtil = new SupabaseUtil(
            baseUrl, 
            "abc", 
            "test_bucket", 
            1L
        );

        client = new AvatarStorageClientOfSupabase(restClient, supabaseUtil);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("genarateUrlToRetrieveAnObject() 実行で結果が200で avatarURL が返る")
    void genarateUrlToRetrieveAnObject_returnSignedUrl() throws InterruptedException {
        String path = "test.png";
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader("content-type", "application/json")
                .setBody("""
                        { "signedURL": "?token=123"}
                        """)
        );

        String expected = baseUrl + "/storage/v1/object/sign/test_bucket/test.png?token=123";

        String result = client.selectAvatar(path);

        assertThat(result).isEqualTo(expected);

        var request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer abc");
    }
    
    @Test
    @DisplayName("genarateUrlToRetrieveAnObject() 実行で結果が200以外かつレスポンスを形式に変換できれば SupabaseStorageException")
    void genarateUrlToRetrieveAnObject_returnSupabaseStorageException() throws InterruptedException {
        String path = "test.png";
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(400)
                .setHeader("content-type", "application/json")
                .setBody("""
                        { "statuscode": "400", "error": "any", "message": "エラー"}
                        """)
        );

        assertThatThrownBy(() -> client.selectAvatar(path))
            .isInstanceOf(SupabaseStorageException.class);

        var request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer abc");
    }

    @Test
    @DisplayName("genarateUrlToRetrieveAnObject() 実行で結果が200以外かつレスポンスを形式に変換できなければ StorageUnavailableException")
    void genarateUrlToRetrieveAnObject_returnStorageUnavailableException() throws InterruptedException {
        String path = "test.png";
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(500)
                .setHeader("content-type", "application/json")
                .setBody("""
                        
                        """)
        );

        assertThatThrownBy(() -> client.selectAvatar(path))
            .isInstanceOf(StorageUnavailableException.class);

        var request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer abc");
    }

    @Test
    @DisplayName("アップロード成功で何も返さない")
    void test_uploadAvatar_success() throws InterruptedException {
        String path = "Test.text";
        String fileContent = "some message";
        MultipartFile multipartFile = new MockMultipartFile(
            "test", 
            "Test.text", 
            "text/plain", 
            fileContent.getBytes(StandardCharsets.UTF_8)
        );
        FileSource fileSource = FileSourceFactory.of(multipartFile);
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
        );

        client.uploadAvatar(fileSource, path);

        var request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer abc");
        assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo("text/plain");
    }

    @Test
    @DisplayName("supabase内におけるエラーでSupabaseStorageExceptionスロー")
    void test_uploadAvatar_throwSupabaseStorageException() throws InterruptedException {
        String path = "Test.text";
        String fileContent = "some message";
        MultipartFile multipartFile = new MockMultipartFile(
            "test", 
            "Test.text", 
            "text/plain", 
            fileContent.getBytes(StandardCharsets.UTF_8)
        );
        FileSource fileSource = FileSourceFactory.of(multipartFile);
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(400)
                .setHeader("content-type", "application/json")
                .setBody("""
                        { "statuscode": "400", "error": "any", "message": "エラー"}
                        """)
        );

        assertThatThrownBy(() -> client.uploadAvatar(fileSource, path))
            .isInstanceOf(SupabaseStorageException.class);

        var request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer abc");
        assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo("text/plain");
    }

    @Test
    @DisplayName("supabaseに関係ないエラーはStorageUnavailableExceptionをスロー")
    void test_uploadAvatar_throwStorageUnavailableException() throws InterruptedException {
        String path = "Test.text";
        String fileContent = "some message";
        MultipartFile multipartFile = new MockMultipartFile(
            "test", 
            "Test.text", 
            "text/plain", 
            fileContent.getBytes(StandardCharsets.UTF_8)
        );
        FileSource fileSource = FileSourceFactory.of(multipartFile);
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(500)
        );

        assertThatThrownBy(() -> client.uploadAvatar(fileSource, path))
            .isInstanceOf(StorageUnavailableException.class);

        var request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer abc");
        assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo("text/plain");
    }

    @Test
    @DisplayName("削除成功で何も返さない")
    void test_deleteAvatars_success() throws InterruptedException {
        List<String> prefixes = List.of("test1.png", "test2.png", "test3.png");
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
        );

        client.deleteAvatars(prefixes);

        var request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("DELETE");
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer abc");
    }

    @Test
    @DisplayName("supabase内におけるエラーでSupabaseStorageExceptionスロー")
    void test_deleteAvatars_throwSupabaseException() throws InterruptedException {
        List<String> prefixes = List.of("test1.png", "test2.png", "test3.png");
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(400)
                .setHeader("content-type", "application/json")
                .setBody("""
                        { "statuscode": "400", "error": "any", "message": "エラー"}
                        """)
        );

        assertThatThrownBy(() -> client.deleteAvatars(prefixes))
            .isInstanceOf(SupabaseStorageException.class);

        var request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("DELETE");
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer abc");
    }

    @Test
    @DisplayName("supabaseに関係ないエラーはStorageUnavailableExceptionをスロー")
    void test_deleteAvatars_throwStorageUnavailableException() throws InterruptedException {
        List<String> prefixes = List.of("test1.png", "test2.png", "test3.png");
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(500)
        );

        assertThatThrownBy(() -> client.deleteAvatars(prefixes))
            .isInstanceOf(StorageUnavailableException.class);

        var request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("DELETE");
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer abc");
    }
}
