package com.weiwei.lease.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weiwei.lease.model.entity.ApartmentInfo;
import com.weiwei.lease.model.entity.FacilityInfo;
import com.weiwei.lease.model.entity.LabelInfo;
import com.weiwei.lease.model.enums.ItemType;
import com.weiwei.lease.web.app.mapper.*;
import com.weiwei.lease.web.app.service.ApartmentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.lease.web.app.service.LabelInfoService;
import com.weiwei.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.weiwei.lease.web.app.vo.apartment.ApartmentItemVo;
import com.weiwei.lease.web.app.vo.graph.GraphVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {
    @Resource
    private  ApartmentInfoMapper apartmentInfoMapper;
    @Resource
    private LabelInfoMapper labelInfoMapper;
    @Resource
    private GraphInfoMapper graphInfoMapper;
    @Resource
    private RoomInfoMapper roomInfoMapper;
    @Resource
    private FacilityInfoMapper facilityInfoMapper;
    @Override
    public ApartmentItemVo getApartmentInfoById(Long id) {
        ApartmentItemVo apartmentItemVo = new ApartmentItemVo();
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, id);
        BigDecimal minRent = roomInfoMapper.selectMinRentByApartmentId(id);

        BeanUtils.copyProperties(apartmentInfo, apartmentItemVo);
        apartmentItemVo.setLabelInfoList(labelInfoList);
        apartmentItemVo.setGraphVoList(graphVoList);
        apartmentItemVo.setMinRent(minRent);

        return apartmentItemVo;
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentId(id);
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, id);
        BigDecimal minRent = roomInfoMapper.selectMinRentByApartmentId(id);

        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();

        BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setMinRent(minRent);
        return apartmentDetailVo;
    }
}




