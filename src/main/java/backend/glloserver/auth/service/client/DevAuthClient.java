package backend.glloserver.auth.service.client;

import backend.glloserver.auth.service.ApplePublicKeyProvider;

import backend.glloserver.global.exception.CustomException;
import org.springframework.web.client.RestClient;

public class DevAuthClient extends ProdAuthClient{

    public DevAuthClient(RestClient restClient,ApplePublicKeyProvider applePublicKeyProvider) {
        super(restClient,applePublicKeyProvider);
    }

    @Override
    protected String getGoogleUserInfo(String accessToken) {
        try {
            return super.getGoogleUserInfo(accessToken);
        } catch (CustomException e) {
            return accessToken;
        }
    }

}
