package io.easycrud.core.config.listenser;

import io.easycrud.core.base.entity.BaseEntityAudit;
import io.easycrud.core.config.header.CommonHeaderManager;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

@Component
public class AuditEventListener {

    @PrePersist
    public void onPrePersist(BaseEntityAudit entity) {
        String username = getCurrentUsername();
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
    }

    @PreUpdate
    public void onPreUpdate(BaseEntityAudit entity) {
        String username = getCurrentUsername();
        entity.setUpdatedBy(username);
    }

    private String getCurrentUsername() {
        return CommonHeaderManager.getCommonHeaders().getUserId();
    }
}
