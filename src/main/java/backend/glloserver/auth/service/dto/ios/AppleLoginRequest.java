package backend.glloserver.auth.service.dto.ios;

public record AppleLoginRequest (String authorizationCode, String tokenId){
}
