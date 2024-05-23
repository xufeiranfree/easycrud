package io.easycrud.core.util.snowflake;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class SnowflakeIdGenerator implements IdentifierGenerator, Configurable {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return Snowflake.getBean().nextId();
    }
}
