package backend.glloserver.auth.service.dto.ios;

public record ApplePublicKey(String kty, String kid, String alg, String n, String e) {

}
