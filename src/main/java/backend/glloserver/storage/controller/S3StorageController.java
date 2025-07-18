package backend.glloserver.storage.controller;

import backend.glloserver.storage.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class S3StorageController {
    private final S3StorageService s3StorageService;

    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam("fileName") String fileName) {
        return ResponseEntity.ok(s3StorageService.getPreSignedUrl("gllo", fileName));
    }
}
