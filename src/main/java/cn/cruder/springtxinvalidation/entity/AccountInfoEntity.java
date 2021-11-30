package cn.cruder.springtxinvalidation.entity;

import cn.cruder.springtxinvalidation.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @Author: cruder
 * @Date: 2021/11/29/22:21
 */
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account_info")
@Entity(name = "account_info")
@Table(appliesTo = "account_info", comment = "账户金额信息表")
@ApiModel("账户金额信息表")
public class AccountInfoEntity extends BaseEntity {

    @ApiModelProperty(value = "账户金额")
    @Column(columnDefinition = "int(1) COMMENT '账户金额'")
    private Integer amount;

}
