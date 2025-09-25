package com.weiwei.lease.web.admin.vo.apartment;

import com.weiwei.lease.model.entity.ApartmentInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import lombok.Data;


@Data
@Schema(description = "后台管理系统公寓列表实体")
public class ApartmentItemVo extends ApartmentInfo {


    @Schema(description = "房间总数")
    private Long totalRoomCount;

    @Schema(description = "空闲房间数")
    private Long freeRoomCount;

}
