package io.easycrud.example.pojo;

import io.easycrud.core.base.entity.BaseDO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "example")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ExampleDO extends BaseDO {
    private String test;
}
