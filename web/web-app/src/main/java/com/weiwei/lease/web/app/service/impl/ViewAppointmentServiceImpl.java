package com.weiwei.lease.web.app.service.impl;

import com.weiwei.lease.model.entity.ApartmentInfo;
import com.weiwei.lease.model.entity.LabelInfo;
import com.weiwei.lease.model.entity.ViewAppointment;
import com.weiwei.lease.model.enums.ItemType;
import com.weiwei.lease.web.app.mapper.*;
import com.weiwei.lease.web.app.service.ApartmentInfoService;
import com.weiwei.lease.web.app.service.ViewAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.lease.web.app.vo.apartment.ApartmentItemVo;
import com.weiwei.lease.web.app.vo.appointment.AppointmentDetailVo;
import com.weiwei.lease.web.app.vo.appointment.AppointmentItemVo;
import com.weiwei.lease.web.app.vo.graph.GraphVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【view_appointment(预约看房信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class ViewAppointmentServiceImpl extends ServiceImpl<ViewAppointmentMapper, ViewAppointment>
        implements ViewAppointmentService {

    @Resource
    private ViewAppointmentMapper viewAppointmentMapper;

    @Resource
    private LabelInfoMapper labelInfoMapper;

    @Resource
    private GraphInfoMapper graphInfoMapper;

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Resource
    private ApartmentInfoMapper apartmentInfoMapper;

    @Resource
    private ApartmentInfoService apartmentInfoService;
    @Override
    public List<AppointmentItemVo> getAppointmentItemVo(Long userId) {

        return viewAppointmentMapper.getAppointmentItemVo(userId);
    }

    @Override
    public AppointmentDetailVo getDetailById(Long id) {
        ViewAppointment viewAppointment = viewAppointmentMapper.selectById(id);

        ApartmentItemVo apartmentItemVo = apartmentInfoService.getApartmentInfoById(viewAppointment.getApartmentId());

        AppointmentDetailVo appointmentDetailVo = new AppointmentDetailVo();

        BeanUtils.copyProperties(viewAppointment,appointmentDetailVo);
        appointmentDetailVo.setApartmentItemVo(apartmentItemVo);

        return appointmentDetailVo;
    }
}




