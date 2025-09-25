package com.weiwei.lease.web.admin.schedule;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScheduleTasksTest {
    @Resource
    private ScheduleTasks scheduleTasks;
    @Test
    public void checkLeaseStatus() {
        scheduleTasks.checkLeaseStatus();
    }

}