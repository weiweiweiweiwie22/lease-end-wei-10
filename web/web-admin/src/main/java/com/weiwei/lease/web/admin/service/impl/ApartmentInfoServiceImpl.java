package com.weiwei.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.lease.common.result.ResultCodeEnum;
import com.weiwei.lease.model.entity.*;
import com.weiwei.lease.model.enums.ItemType;
import com.weiwei.lease.common.exception.BusinessException;
import com.weiwei.lease.web.admin.mapper.*;
import com.weiwei.lease.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.weiwei.lease.web.admin.vo.fee.FeeValueVo;
import com.weiwei.lease.web.admin.vo.graph.GraphVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Resource
    private FeeValueMapper feeValueMapper;

    @Resource
    private FacilityInfoMapper facilityInfoMapper;

    @Resource
    private ApartmentInfoMapper apartmentInfoMapper;

    @Resource
    private GraphInfoService graphInfoService;

    @Resource
    private ApartmentFacilityService apartmentFacilityService;

    @Resource
    private ApartmentLabelService apartmentLabelService;
    @Resource
    private ApartmentLabelMapper apartmentLabelMapper;

    @Resource
    private ApartmentFeeValueService apartmentFeeValueService;
    @Resource
    private GraphInfoMapper graphInfoMapper;
    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Override
    @Transactional
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
        boolean isUpdate = apartmentSubmitVo.getId() != null;
        super.saveOrUpdate(apartmentSubmitVo);
        if (isUpdate){
            //删除图片
            LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemId, apartmentSubmitVo.getId());
            graphInfoService.remove(graphInfoLambdaQueryWrapper);

            //删除配套
            LambdaQueryWrapper<ApartmentFacility> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(ApartmentFacility::getApartmentId, apartmentSubmitVo.getId());
            apartmentFacilityService.remove(queryWrapper2);

            //删除标签
            LambdaQueryWrapper<ApartmentLabel> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(ApartmentLabel::getApartmentId, apartmentSubmitVo.getId());
            apartmentLabelService.remove(queryWrapper3);

            //删除费用
            LambdaQueryWrapper<ApartmentFeeValue> queryWrapper4 = new LambdaQueryWrapper<>();
            queryWrapper4.eq(ApartmentFeeValue::getApartmentId, apartmentSubmitVo.getId());
            apartmentFeeValueService.remove(queryWrapper4);

        }
        //保存图片
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        if (!CollectionUtils.isEmpty(graphVoList)){
            ArrayList<GraphInfo> graphInfoList = new ArrayList<>();

            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setItemId(apartmentSubmitVo.getId());
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfoList.add(graphInfo);
            }
            graphInfoService.saveOrUpdateBatch(graphInfoList);
        }
        //保存配套
        List<Long> facilityInfoIds = apartmentSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityInfoIds)){
            ArrayList<ApartmentFacility> apartmentFacilityList = new ArrayList<>();
            for (Long facilityInfoId : facilityInfoIds) {
                ApartmentFacility apartmentFacility = ApartmentFacility.builder()
                        .apartmentId(apartmentSubmitVo.getId())
                        .facilityId(facilityInfoId)
                        .build();
                apartmentFacilityList.add(apartmentFacility);
            }
            apartmentFacilityService.saveOrUpdateBatch(apartmentFacilityList);
        }
        //保存标签
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!CollectionUtils.isEmpty(labelIds)){
            ArrayList<ApartmentLabel> apartmentLabelList = new ArrayList<>();
            for (Long labelId : labelIds) {
                ApartmentLabel apartmentLabel = ApartmentLabel.builder()
                        .apartmentId(apartmentSubmitVo.getId())
                        .labelId(labelId)
                        .build();
                        apartmentLabelList.add(apartmentLabel);
            }
            apartmentLabelService.saveOrUpdateBatch(apartmentLabelList);
        }
        //保存费用
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!CollectionUtils.isEmpty(feeValueIds)){
            ArrayList<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>();
            for (Long feeValueId : feeValueIds) {
                ApartmentFeeValue apartmentFeeValue = ApartmentFeeValue.builder()
                        .apartmentId(apartmentSubmitVo.getId())
                        .feeValueId(feeValueId)
                        .build();
                apartmentFeeValueList.add(apartmentFeeValue);
            }
            apartmentFeeValueService.saveOrUpdateBatch(apartmentFeeValueList);
        }


    }

    @Override
    public IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo) {

        return apartmentInfoMapper.pageItem(page,queryVo);
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        //查询公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
        //查询该公寓图片列表
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT,id);
        //查询该公寓标签列表
        List<LabelInfo> labelInfoList = apartmentLabelMapper.selectListById(id);
        //查询该公寓配套列表
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentId(id);
        //查询该公寓费用列表
        List<FeeValueVo> feeValueInfoList = feeValueMapper.selectListById(id);
        //组装
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo,apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setFeeValueVoList(feeValueInfoList);
        return apartmentDetailVo;
    }

    @Override
    public void removeApartmentById(Long id) {
        LambdaQueryWrapper<RoomInfo> roomInfoQueryWrapper = new LambdaQueryWrapper<>();
        roomInfoQueryWrapper.eq(RoomInfo::getApartmentId,id);
        Long count = roomInfoMapper.selectCount(roomInfoQueryWrapper);
        if (count > 0){
            throw new BusinessException(ResultCodeEnum.DELETE_ERROR);
        }
        super.removeById(id);
        //删除图片
        LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
        graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemId, id);
        graphInfoService.remove(graphInfoLambdaQueryWrapper);

        //删除配套
        LambdaQueryWrapper<ApartmentFacility> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(ApartmentFacility::getApartmentId, id);
        apartmentFacilityService.remove(queryWrapper2);

        //删除标签
        LambdaQueryWrapper<ApartmentLabel> queryWrapper3 = new LambdaQueryWrapper<>();
        queryWrapper3.eq(ApartmentLabel::getApartmentId, id);
        apartmentLabelService.remove(queryWrapper3);

        //删除费用
        LambdaQueryWrapper<ApartmentFeeValue> queryWrapper4 = new LambdaQueryWrapper<>();
        queryWrapper4.eq(ApartmentFeeValue::getApartmentId, id);
        apartmentFeeValueService.remove(queryWrapper4);
    }
}




