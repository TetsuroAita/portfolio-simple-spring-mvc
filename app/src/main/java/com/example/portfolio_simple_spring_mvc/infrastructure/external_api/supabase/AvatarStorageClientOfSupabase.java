package com.example.portfolio_simple_spring_mvc.infrastructure.external_api.supabase;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSource;
import com.example.portfolio_simple_spring_mvc.domain.port.storage.AvatarStorageClient;
import com.example.portfolio_simple_spring_mvc.infrastructure.exception.StorageUnavailableException;
import com.example.portfolio_simple_spring_mvc.infrastructure.exception.SupabaseStorageException;
import com.example.portfolio_simple_spring_mvc.infrastructure.external_api.dto.ApiRequest;
import com.example.portfolio_simple_spring_mvc.infrastructure.external_api.dto.ApiResponse;

@Component("avatarStorageClientOfSupabase")
public class AvatarStorageClientOfSupabase implements AvatarStorageClient {
    private final Logger logger = LoggerFactory.getLogger(AvatarStorageClientOfSupabase.class);
    private final RestClient restClient;
    private final SupabaseUtil util;

    public AvatarStorageClientOfSupabase(
        RestClient restClient,
        SupabaseUtil util
    ) {
        this.restClient = restClient;
        this.util = util;
    }

    @Override
    public String selectAvatar(String path) {
        try {
            return restClient.post()
                .uri(util.genarateURLtoRetrieveAnObjectURI(path))
                .header(HttpHeaders.AUTHORIZATION, util.apiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(util.expiresIn())
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        String presignedURL = response.bodyTo(ApiResponse.Supabase_GeneratedPresignedUrl.class).signedURL();
                        String token = util.pickOutToken(presignedURL);
                        String avatarURL = util.generateAvatarURL(path, token);
                        return avatarURL;
                    } else if (response.getStatusCode().is4xxClientError()) {
                        ApiResponse.Supabase_Error apiResponse = response.bodyTo(ApiResponse.Supabase_Error.class);
                        throw new SupabaseStorageException(String.format("SelectAvatar=\"%s\", error=%s", path, apiResponse));
                    } else {
                        throw new StorageUnavailableException();
                    }
                });
        } catch (SupabaseStorageException e) {
            throw e;
        } catch (Exception e) {
            throw new StorageUnavailableException(e);
        }
    }

    @Override
    public void uploadAvatar(FileSource fileSource, String path) {
        String contentType = fileSource.getInfo().contentType();

        try (InputStream is = fileSource.openStream()) {
            Resource resource = new InputStreamResource(is);
            restClient.post()
                .uri(util.uploadAnObjectURI(path))
                .header(HttpHeaders.AUTHORIZATION, util.apiKey())
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return null;
                    } else if (response.getStatusCode().is4xxClientError()) {
                        ApiResponse.Supabase_Error apiResponse = response.bodyTo(ApiResponse.Supabase_Error.class);
                        throw new SupabaseStorageException(String.format("UploadAvatar=%s, \"%s\", error=%s", fileSource, path, apiResponse));
                    } else {
                        throw new StorageUnavailableException();
                    }
                });
        } catch (SupabaseStorageException e) {
            throw e;
        } catch (Exception e) {
            throw new StorageUnavailableException(e);
        }
    }

    @Override
    public void deleteAvatars(List<String> prefixes) {
        logger.debug("削除対象={}, 件数={}", prefixes, prefixes.size());

        try {
            restClient.method(HttpMethod.DELETE)
                .uri(util.deleteMultipleObjectsURI())
                .header(HttpHeaders.AUTHORIZATION, util.apiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiRequest.prefixes(prefixes))
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        List<ApiResponse.Supabase_DeletedObjectName> result =
                            response.bodyTo(new ParameterizedTypeReference<List<ApiResponse.Supabase_DeletedObjectName>>() {});
                        logger.debug("削除完了={}", result);
                        return null;
                    } else if (response.getStatusCode().is4xxClientError()) {
                        ApiResponse.Supabase_Error apiResponse = response.bodyTo(ApiResponse.Supabase_Error.class);
                        throw new SupabaseStorageException(String.format("DeleteAvatars=%s, error=%s", prefixes, apiResponse));
                    } else {
                        throw new StorageUnavailableException();
                    }
                });
        } catch (SupabaseStorageException e) {
            throw e;
        } catch (Exception e) {
            throw new StorageUnavailableException(e);
        }
    }
}
