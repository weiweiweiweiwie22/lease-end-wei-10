package com.weiwei.lease.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.lease.model.entity.BrowsingHistory;
import com.weiwei.lease.web.app.mapper.BrowsingHistoryMapper;
import com.weiwei.lease.web.app.service.BrowsingHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.lease.web.app.vo.history.HistoryItemVo;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author liubo
 * @description 针对表【browsing_history(浏览历史)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class BrowsingHistoryServiceImpl extends ServiceImpl<BrowsingHistoryMapper, BrowsingHistory>
        implements BrowsingHistoryService {
    @Resource
    private BrowsingHistoryMapper browsingHistoryMapper;

    @Override
    public IPage<HistoryItemVo> pageItem(Page<HistoryItemVo> historyItemVoPage, Long userId) {
        return browsingHistoryMapper.pageItemByUserId(historyItemVoPage,userId);
    }


    // 保存浏览记录
    @Override
    @Async
    public void saveHistory(Long userId, Long id) {
        System.out.println("保存浏览历史" + Thread.currentThread().getName());

        LambdaQueryWrapper<BrowsingHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BrowsingHistory::getUserId,userId);
        queryWrapper.eq(BrowsingHistory::getId,id);
        BrowsingHistory browsingHistory = browsingHistoryMapper.selectOne(queryWrapper);
        if (browsingHistory != null){
            browsingHistory.setBrowseTime(new Date());
            browsingHistoryMapper.updateById(browsingHistory);
        }else {
            BrowsingHistory browsingHistory1 = new BrowsingHistory(userId,id,new Date());
            browsingHistoryMapper.insert(browsingHistory1);
        }

    }
}