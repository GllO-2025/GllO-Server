package backend.glloserver.auth.controller;

import backend.glloserver.auth.service.AuthService;
import backend.glloserver.auth.service.dto.ios.AppleLoginRequest;
import backend.glloserver.auth.service.dto.AuthInfoDto;
import backend.glloserver.auth.service.dto.GoogleLoginRequest;
import backend.glloserver.auth.service.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;


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

    @PostMapping("/auth/login/apple")
    public ResponseEntity<LoginResponse> appleLogin(@RequestBody AppleLoginRequest request){
        AuthInfoDto authInfo = authService.appleLogin(request);
        LoginResponse response = new LoginResponse(authInfo.authMember());
        return ResponseEntity.ok(response);
    }

    //리디렉션용
    @PostMapping("/login/oauth2/code/apple")
    public ResponseEntity<String> handleApppleCallback(@RequestParam Map<String,String> params){
        String code=params.get("code"); //authorization_code
        String idToken=params.get("id_token"); //id_token

        log.debug("code: "+code);
        log.debug("idToken: "+idToken);

        return ResponseEntity.ok("Apple callback received");
    }

}
