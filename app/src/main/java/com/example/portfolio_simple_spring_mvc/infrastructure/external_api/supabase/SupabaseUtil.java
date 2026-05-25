package com.example.portfolio_simple_spring_mvc.infrastructure.external_api.supabase;

import com.example.portfolio_simple_spring_mvc.infrastructure.external_api.dto.ApiRequest.Supabase_ExpiresObjectURL;

class SupabaseUtil {
    private final String projectUrl;
    private final String apiKey;
    private final String bucketName;
    private final long expiresIn;

    SupabaseUtil(
        String projectUrl,
        String apiKey,
        String bucketName,
        long expiresIn
    ) {
        this.projectUrl = projectUrl;
        this.apiKey = apiKey;
        this.bucketName = bucketName;
        this.expiresIn = expiresIn;
    }

    // avatarURL 生成
    String generateAvatarURL(String path, String token) {
        return generateURI("/storage/v1/object/sign/") + "/" + path + "?token=" + token;
    }

    // 取得した presignedURl からトークンのみ取り出す
    String pickOutToken(String response) {
        return response.substring(response.indexOf("=") + 1);
    }

    // ===== SupabaseStorageCliet =====
    String apiKey() {
        return "Bearer " + apiKey;
    }

    Supabase_ExpiresObjectURL expiresIn() {
        return new Supabase_ExpiresObjectURL(expiresIn);
    }

    String genarateURLtoRetrieveAnObjectURI(String path) {
        return generateURI("/storage/v1/object/sign/") + "/" + path;
    }

    String uploadAnObjectURI(String path) {
        return generateURI("/storage/v1/object/") + "/" + path;
    }

    String deleteMultipleObjectsURI() {
        return generateURI("/storage/v1/object/");
    }

    private String generateURI(String partOfUri) {
        return projectUrl + partOfUri + bucketName;
    }
}
