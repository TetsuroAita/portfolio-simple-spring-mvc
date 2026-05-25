package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.service.reader;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import com.example.portfolio_simple_spring_mvc.domain.model.pagination.DomainPageRequest;
import com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository.ProfileRepository;

@ExtendWith(MockitoExtension.class)
public class ProfileReaderOfJpaRepoTest {
    @Mock
    private ProfileRepository profileRepository;
    
    @InjectMocks
    private ProfileReaderOfJpaRepo reader;
    
    private DomainPageRequest request;

    @BeforeEach
    public void setUp() {
        request = new DomainPageRequest(0, 10, "any", true);
    }

    @Test
    @DisplayName("activity が true で findByActiveTrue が呼ばれる")
    void test_givenTrue_verifyFindByActiveTrue() {

        when(profileRepository.findByActiveTrue(any())).thenReturn(Page.empty());
        reader.selectProfiles(true, request);
        verify(profileRepository).findByActiveTrue(any());
    }

    @Test
    @DisplayName("activity が false で findByActiveFalse が呼ばれる")
    void test_givenFalse_verifyFindByActiveFalse() {
        when(profileRepository.findByActiveFalse(any())).thenReturn(Page.empty());
        reader.selectProfiles(false, request);
        verify(profileRepository).findByActiveFalse(any());
    }
}
