package com.u.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 群组日收支统计表
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("day_statistics_group")
@ApiModel(value="DayStatisticsGroup对象", description="群组日收支统计表")
public class DayStatisticsGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "day_sta_id", type = IdType.AUTO)
    private Integer dayStaId;

    @ApiModelProperty(value = "收入或支出 1表示收入0表示支出")
    private Integer transactionType;

    @ApiModelProperty(value = "金额")
    private Float staAmount;

    @ApiModelProperty(value = "日期")
    private LocalDateTime date;


}
