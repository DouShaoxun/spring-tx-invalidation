package cn.cruder.springtxinvalidation.service;

import cn.cruder.springtxinvalidation.constant.Constant;
import cn.cruder.springtxinvalidation.entity.AccountInfoEntity;
import cn.cruder.springtxinvalidation.mapper.AccountInfoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;

/**
 * @Author: cruder
 * @Date: 2021/11/30/22:10
 */
@Service
@AllArgsConstructor
public class AccountInfoServiceA {
    private final AccountInfoMapper accountInfoMapper;

    /**
     * 转账
     * <br/>
     * 如果amount大于10 则抛出{@link FileNotFoundException}异常
     *
     * @param from   from
     * @param to     to
     * @param amount 金额
     * @throws FileNotFoundException
     */
    @Transactional
    public void transferA(Long from, Long to, Integer amount) throws FileNotFoundException {
        AccountInfoEntity fromEntity = accountInfoMapper.selectById(from);
        Integer entityAmount = fromEntity.getAmount();
        if (entityAmount - amount >= 0) {
            fromEntity.setAmount(entityAmount - amount);
            AccountInfoEntity toEntity = accountInfoMapper.selectById(to);
            toEntity.setAmount(toEntity.getAmount() + amount);
            accountInfoMapper.updateById(fromEntity);
            if (amount > Constant.EXCEPTION_CRITICAL_VALUE) {
                throw new FileNotFoundException();
            }
            accountInfoMapper.updateById(toEntity);
        }
    }

    /**
     * FileNotFoundException是Exception的子类，所以指定rollbackFor = Exception.class会回滚
     * <br/>
     * 如果不指定默认为RuntimeException和Error 的子类会回滚
     *
     * @param from
     * @param to
     * @param amount
     * @throws FileNotFoundException
     */
    @Transactional(rollbackFor = Exception.class)
    public void transferB(Long from, Long to, Integer amount) throws FileNotFoundException {
        AccountInfoEntity fromEntity = accountInfoMapper.selectById(from);
        Integer entityAmount = fromEntity.getAmount();
        if (entityAmount - amount >= 0) {
            fromEntity.setAmount(entityAmount - amount);
            AccountInfoEntity toEntity = accountInfoMapper.selectById(to);
            toEntity.setAmount(toEntity.getAmount() + amount);
            accountInfoMapper.updateById(fromEntity);
            if (amount > Constant.EXCEPTION_CRITICAL_VALUE) {
                throw new FileNotFoundException();
            }
            accountInfoMapper.updateById(toEntity);
        }
    }

}
