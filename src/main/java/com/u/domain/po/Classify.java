package com.u.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支出分类表
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("classify")
@ApiModel(value="Classify对象", description="支出分类表")
public class Classify implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "classify_id", type = IdType.AUTO)
    private Integer classifyId;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "收支类型属于哪一层")
    private Integer level;

    @ApiModelProperty(value = "收入或支出 1表示收入 0表示支出")
    private Integer transactionType;


}
