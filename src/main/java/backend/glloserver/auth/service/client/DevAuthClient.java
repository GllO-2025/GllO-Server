package backend.glloserver.auth.service.client;

import backend.glloserver.global.exception.CustomException;
import org.springframework.web.client.RestClient;

public class DevAuthClient extends ProdAuthClient{

    public DevAuthClient(RestClient restClient) {
        super(restClient);
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
