package com.weiwei.lease.web.app.service.impl;

import com.weiwei.lease.model.entity.PaymentType;
import com.weiwei.lease.web.app.mapper.PaymentTypeMapper;
import com.weiwei.lease.web.app.service.PaymentTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liubo
* @description 针对表【payment_type(支付方式表)】的数据库操作Service实现
* @createDate 2023-07-26 11:12:39
*/
@Service
public class PaymentTypeServiceImpl extends ServiceImpl<PaymentTypeMapper, PaymentType>
    implements PaymentTypeService{

    @Resource
    private PaymentTypeMapper paymentTypeMapper;
    @Override
    public List<PaymentType> getTypesByRoomId(Long id) {
        return paymentTypeMapper.selectListByRoomId(id);
    }
}




