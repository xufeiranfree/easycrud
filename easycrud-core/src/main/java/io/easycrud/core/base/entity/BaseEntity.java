package io.easycrud.core.base.entity;

import io.easycrud.core.util.snowflake.SnowflakeIdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Version;

import java.io.Serial;
import java.io.Serializable;

@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2375924982927170763L;

    @Id
    @GeneratedValue(generator = "snowflakeIdGenerator")
    @GenericGenerator(name = "snowflakeIdGenerator", type = SnowflakeIdGenerator.class)
    @Column(name = "id", length = 18, updatable = false, nullable = false)
    private String id;

    @Version
    @Column(name = "version")
    private Integer version;

}
