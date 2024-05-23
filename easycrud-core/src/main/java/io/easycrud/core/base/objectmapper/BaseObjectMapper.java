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

    List<VO> doToVOs(Collection<DO> ds);

    VO poToVO(PO p);

    List<VO> poToVOs(Collection<PO> ps);

    // to do
    DO dtoToDO(DTO dt);

    List<DO> dtoToVOs(Collection<DTO> dts);

    // to po
    PO dtoToPO(DTO dt);

    List<PO> dtoToPOs(Collection<DTO> dts);
}
