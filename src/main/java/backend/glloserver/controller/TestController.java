package backend.glloserver.controller;

import backend.glloserver.dto.test.TestDTO;
import backend.glloserver.global.ApiResponse;
import backend.glloserver.global.status.SuccessStatus;
import backend.glloserver.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/test")
    ApiResponse<TestDTO> test(@RequestParam Integer id) {
        TestDTO result=testService.getTestById(id);
        return ApiResponse.onSuccess(SuccessStatus._OK,result);

    }
}
