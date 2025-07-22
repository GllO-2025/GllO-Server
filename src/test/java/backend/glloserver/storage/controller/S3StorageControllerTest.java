package backend.glloserver.storage.controller;

import backend.glloserver.auth.config.AuthWebMvcConfig;
import backend.glloserver.auth.service.JwtTokenProvider;
import backend.glloserver.global.config.TestConfig;
import backend.glloserver.storage.service.S3StorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@WebMvcTest(controllers = S3StorageController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthWebMvcConfig.class))
class S3StorageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private S3StorageService s3StorageService;

    @Test
    @DisplayName("Presigned URL 발급 성공")
    void getPresignedUrlSuccess() throws Exception {
        // given
        String fileName = "test.jpg";
        String expectedUrl = "https://example.com/presigned-url";
        Mockito.when(s3StorageService.getPreSignedUrl(eq("gllo"), eq(fileName)))
                .thenReturn(expectedUrl);

        // when then
        mockMvc.perform(get("/api/v1/storage/presigned-url")
                        .param("fileName", fileName))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedUrl));
    }

}