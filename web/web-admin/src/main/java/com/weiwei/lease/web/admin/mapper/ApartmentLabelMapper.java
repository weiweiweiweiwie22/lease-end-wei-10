package com.weiwei.lease.web.admin.mapper;

import com.weiwei.lease.model.entity.ApartmentLabel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weiwei.lease.model.entity.LabelInfo;

import java.util.List;

/**
* @author liubo
* @description 针对表【apartment_label(公寓标签关联表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.weiwei.lease.model.ApartmentLabel
*/
public interface ApartmentLabelMapper extends BaseMapper<ApartmentLabel> {

    List<LabelInfo> selectListById(Long id);
}




