package com.weiwei.lease.web.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.lease.common.constant.RedisConstant;
import com.weiwei.lease.common.login.LoginUserHolder;
import com.weiwei.lease.model.entity.*;
import com.weiwei.lease.model.enums.ItemType;
import com.weiwei.lease.web.app.mapper.*;
import com.weiwei.lease.web.app.service.ApartmentInfoService;
import com.weiwei.lease.web.app.service.BrowsingHistoryService;
import com.weiwei.lease.web.app.service.RoomInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.lease.web.app.vo.apartment.ApartmentItemVo;
import com.weiwei.lease.web.app.vo.attr.AttrValueVo;
import com.weiwei.lease.web.app.vo.fee.FeeValueVo;
import com.weiwei.lease.web.app.vo.graph.GraphVo;
import com.weiwei.lease.web.app.vo.room.RoomDetailVo;
import com.weiwei.lease.web.app.vo.room.RoomItemVo;
import com.weiwei.lease.web.app.vo.room.RoomQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
@Slf4j
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {

    @Resource
    private ApartmentInfoService apartmentInfoService;

    @Resource
    private RoomInfoMapper roomInfoMapper;
    @Resource
    private GraphInfoMapper graphInfoMapper;

    @Resource
    private AttrValueMapper attrValueMapper;

    @Resource
    private FacilityInfoMapper facilityInfoMapper;
    @Resource
    private LabelInfoMapper labelInfoMapper;

    @Resource
    private PaymentTypeMapper paymentTypeMapper;

    @Resource
    private LeaseTermMapper leaseTermMapper;

    @Resource
    private FeeValueMapper feeValueMapper;

    @Resource
    private BrowsingHistoryService browsingHistoryService;

    @Autowired
    private RedisTemplate<String , Object> redisTemplate;
    @Override
    public IPage<RoomItemVo> pageItem(IPage<RoomItemVo> page, RoomQueryVo queryVo) {

        return roomInfoMapper.pageItem(page, queryVo);
    }

    @Override
    public RoomDetailVo getDetailById(Long id) {
        String key = RedisConstant.APP_ROOM_PREFIX + id;
        RoomDetailVo roomDetailVo = (RoomDetailVo)redisTemplate.opsForValue().get(key);
        if (roomDetailVo == null){
            RoomInfo roomInfo = roomInfoMapper.selectById(id);
            if (roomInfo == null){
                return null;
            }
            ApartmentItemVo apartmentItemVo = apartmentInfoService.getApartmentInfoById(roomInfo.getApartmentId());
            List<GraphVo> graphVoList = graphInfoMapper.selectListGraphByTypeAndId(ItemType.ROOM, id);
            List<AttrValueVo> attrvalueVoList = attrValueMapper.selectListByRoomId(id);
            List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByRoomId(id);
            List<LabelInfo> labelInfoList = labelInfoMapper.selectListByRoomId(id);
            List<PaymentType> paymentTypeList = paymentTypeMapper.selectListByRoomId(id);
            List<LeaseTerm> leaseTermList = leaseTermMapper.selectListByRoomId(id);
            List<FeeValueVo> feeValueVoList = feeValueMapper.selectListByApartmentId(roomInfo.getApartmentId());

            roomDetailVo = new RoomDetailVo();
            BeanUtils.copyProperties(roomInfo,roomDetailVo);

            roomDetailVo.setApartmentItemVo(apartmentItemVo);
            roomDetailVo.setGraphVoList(graphVoList);
            roomDetailVo.setAttrValueVoList(attrvalueVoList);
            roomDetailVo.setFacilityInfoList(facilityInfoList);
            roomDetailVo.setLabelInfoList(labelInfoList);
            roomDetailVo.setPaymentTypeList(paymentTypeList);
            roomDetailVo.setLeaseTermList(leaseTermList);
            roomDetailVo.setFeeValueVoList(feeValueVoList);

            redisTemplate.opsForValue().set(key, roomDetailVo);
        }



        browsingHistoryService.saveHistory(LoginUserHolder.getLoginUser().getUserId(), id);

        System.out.println("房间详情" + Thread.currentThread().getName());

        return roomDetailVo;
    }

    @Override
    public IPage<RoomItemVo> pageItemByApartmentId(Page<RoomItemVo> page, Long id) {
        return roomInfoMapper.pageItemByApartmentId(page, id);
    }
}




