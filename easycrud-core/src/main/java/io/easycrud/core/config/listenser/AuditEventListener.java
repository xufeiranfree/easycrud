package io.easycrud.core.config.listenser;

import io.easycrud.core.base.entity.BaseEntityAudit;
import io.easycrud.core.config.header.CommonHeaderManager;
import io.easycrud.core.config.header.CommonHeaders;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditEventListener {

    @PrePersist
    public void onPrePersist(BaseEntityAudit entity) {
        getCurrentUsername().ifPresent(username -> {
            entity.setCreatedBy(username);
            entity.setUpdatedBy(username);
        });
    }

    @PreUpdate
    public void onPreUpdate(BaseEntityAudit entity) {
        getCurrentUsername().ifPresent(entity::setUpdatedBy);
    }

    private Optional<String> getCurrentUsername() {
        return Optional.ofNullable(CommonHeaderManager.getCommonHeaders())
                .map(CommonHeaders::getUserId);
    }
}
