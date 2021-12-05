package cn.cruder.springtxinvalidation.entity.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: cruder
 * @Date: 2021/11/29/22:19
 */

@Data
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "主键id")
    private Long id;

    @JSONField(serialize = false)
    @ApiModelProperty(value = "是否删除，0 未删除， 1 删除")
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

    @JSONField(serialize = false)
    @ApiModelProperty(value = "是否可用，1 可用，0 不可用")
    @TableField(fill = FieldFill.INSERT)
    private Integer available;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS", serialize = false)
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
