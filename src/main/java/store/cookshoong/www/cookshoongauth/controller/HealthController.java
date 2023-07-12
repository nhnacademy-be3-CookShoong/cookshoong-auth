package store.cookshoong.www.cookshoongauth.controller;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버 상태 체크를 위한 컨트롤러.
 *
 * @author koesnam (추만석)
 * @since 2023.07.11
 */
@RestController
@RequiredArgsConstructor
public class HealthController {
    private final ApplicationInfoManager applicationInfoManager;
    @PostMapping("/health-check/fail")
    public ResponseEntity<Void> stop() {
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
        return ResponseEntity.badRequest()
            .build();
    }

    @PostMapping("/health-check/recover")
    public ResponseEntity<Void> start() {
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/health-check")
    public ResponseEntity<InstanceInfo.InstanceStatus> check() {
        return ResponseEntity.ok(applicationInfoManager
            .getInfo()
            .getStatus());
    }
}
