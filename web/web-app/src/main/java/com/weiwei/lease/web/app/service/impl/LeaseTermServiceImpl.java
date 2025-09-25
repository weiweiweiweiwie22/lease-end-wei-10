package com.weiwei.lease.web.app.service.impl;

import com.weiwei.lease.model.entity.LeaseTerm;
import com.weiwei.lease.web.app.mapper.LeaseTermMapper;
import com.weiwei.lease.web.app.service.LeaseTermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【lease_term(租期)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class LeaseTermServiceImpl extends ServiceImpl< LeaseTermMapper, LeaseTerm>
        implements LeaseTermService {

    @Resource
    private LeaseTermMapper leaseTermMapper;
    @Override
    public List<LeaseTerm> getTermsByRoomId(Long id) {
        return leaseTermMapper.selectListByRoomId( id);
    }
}




