package io.easycrud.example.pojo;

import io.easycrud.core.base.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ExampleVO extends BaseVO {
    private String test;
}
