package cn.cruder.springtxinvalidation.service;

import cn.cruder.springtxinvalidation.entity.AccountInfoEntity;
import cn.cruder.springtxinvalidation.mapper.AccountInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: cruder
 * @Date: 2021/12/05/19:48
 */
@Slf4j
@Service
@AllArgsConstructor
public class AccountInfoServiceE {
    @Autowired
    private AccountInfoMapper accountInfoMapper;

    /**
     * 转账
     * <br/>
     * 结合业务，只有当金额大于0的时候才提交事务，但是多线程会出问题
     *
     * @param from   from
     * @param to     to
     * @param amount 金额
     */
    @Transactional(rollbackFor = Exception.class)
    public void transferA(Long from, Long to, Integer amount) {
        AccountInfoEntity fromEntity = accountInfoMapper.selectById(from);
        Integer entityAmount = fromEntity.getAmount();
        if (entityAmount - amount >= 0) {
            fromEntity.setAmount(entityAmount - amount);
            AccountInfoEntity toEntity = accountInfoMapper.selectById(to);
            toEntity.setAmount(toEntity.getAmount() + amount);
            accountInfoMapper.updateById(fromEntity);
            accountInfoMapper.updateById(toEntity);
        }
    }


    /**
     * 此处加synchronized，仍然会出问题：因为Transactional是基于代理对象的，commit执行在目标方法执行之后，
     * 把synchronized加载service调用处可以解决，但是不推荐
     *
     * @param from
     * @param to
     * @param amount
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized void transferB(Long from, Long to, Integer amount) {
        AccountInfoEntity fromEntity = accountInfoMapper.selectById(from);
        Integer entityAmount = fromEntity.getAmount();
        if (entityAmount - amount >= 0) {
            fromEntity.setAmount(entityAmount - amount);
            AccountInfoEntity toEntity = accountInfoMapper.selectById(to);
            toEntity.setAmount(toEntity.getAmount() + amount);
            accountInfoMapper.updateById(fromEntity);
            accountInfoMapper.updateById(toEntity);
        }
    }

    /**
     * 在数据库层级添加查询 for update
     *
     * @param from
     * @param to
     * @param amount
     */
    @Transactional(rollbackFor = Exception.class)
    public void transferC(Long from, Long to, Integer amount) {
        QueryWrapper<AccountInfoEntity> fromWrapper = new QueryWrapper<>();
        fromWrapper.eq("id", from);
        fromWrapper.last(" for update ");
        AccountInfoEntity fromEntity = accountInfoMapper.selectOne(fromWrapper);
        Integer entityAmount = fromEntity.getAmount();
        if (entityAmount - amount >= 0) {
            fromEntity.setAmount(entityAmount - amount);

            QueryWrapper<AccountInfoEntity> toWrapper = new QueryWrapper<>();
            toWrapper.eq("id", to);
            toWrapper.last(" for update ");
            AccountInfoEntity toEntity = accountInfoMapper.selectOne(toWrapper);
            toEntity.setAmount(toEntity.getAmount() + amount);
            accountInfoMapper.updateById(fromEntity);
            accountInfoMapper.updateById(toEntity);
        }
    }
}
