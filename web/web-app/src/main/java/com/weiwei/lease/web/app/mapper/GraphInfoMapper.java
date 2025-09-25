package com.weiwei.lease.web.app.mapper;

import com.weiwei.lease.model.entity.GraphInfo;
import com.weiwei.lease.model.enums.ItemType;
import com.weiwei.lease.web.app.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author liubo
* @description 针对表【graph_info(图片信息表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.weiwei.lease.model.entity.GraphInfo
*/
public interface GraphInfoMapper extends BaseMapper<GraphInfo> {


    List<GraphVo> selectListGraphByTypeAndId(ItemType itemType, Long id);

    List<GraphVo> selectListByItemTypeAndId(ItemType itemType, Long id);
}




