package io.easycrud.core.base.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public class BasePO extends BaseEntityAudit {
}
