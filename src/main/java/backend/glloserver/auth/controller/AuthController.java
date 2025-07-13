package backend.glloserver.auth.controller;

import backend.glloserver.auth.service.AuthService;
import backend.glloserver.auth.service.dto.AuthInfoDto;
import backend.glloserver.auth.service.dto.GoogleLoginRequest;
import backend.glloserver.auth.service.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login/google")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleLoginRequest request){
        log.info("Google login request: {}", request);
        AuthInfoDto authInfo = authService.googleLogin(request);
        LoginResponse response = new LoginResponse(authInfo.authMember());
        return ResponseEntity.ok(response);
    }
}
