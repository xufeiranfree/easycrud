package io.easycrud.core.base.objectmapper;

import io.easycrud.core.base.entity.BaseDO;
import io.easycrud.core.base.entity.BaseDTO;
import io.easycrud.core.base.entity.BasePO;
import io.easycrud.core.base.entity.BaseVO;
import org.mapstruct.MapperConfig;

import java.util.Collection;
import java.util.List;

@MapperConfig
public interface BaseObjectMapper<DTO extends BaseDTO, VO extends BaseVO, DO extends BaseDO, PO extends BasePO> {

    // to vo
    VO doToVO(DO d);

    List<VO> dosToVOs(Collection<DO> ds);

    VO poToVO(PO p);

    List<VO> posToVOs(Collection<PO> ps);

    // to do
    DO dtoToDO(DTO dt);

    List<DO> dtosToVOs(Collection<DTO> dts);

    // to po
    PO dtoToPO(DTO dt);

    List<PO> dtosToPOs(Collection<DTO> dts);
}
