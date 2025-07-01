package backend.glloserver.service;

import backend.glloserver.dto.test.TestDTO;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public TestDTO getTestById(Integer id){return new TestDTO(id,"테스트입니다.");}
}
