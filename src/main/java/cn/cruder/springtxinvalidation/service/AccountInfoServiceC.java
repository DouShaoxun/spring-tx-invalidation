package cn.cruder.springtxinvalidation.service;

import cn.cruder.springtxinvalidation.constant.Constant;
import cn.cruder.springtxinvalidation.entity.AccountInfoEntity;
import cn.cruder.springtxinvalidation.mapper.AccountInfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;

/**
 * @Author: cruder
 * @Date: 2021/12/02/21:00
 */
@Slf4j
@Service
@AllArgsConstructor
public class AccountInfoServiceC {
    private final AccountInfoMapper accountInfoMapper;

    /**
     * 指定rollbackFor = Exception.class了。
     * <br/>
     * 切面捕获了异常 、没抛出处理
     *
     * @param from
     * @param to
     * @param amount
     * @throws FileNotFoundException
     */
    @Transactional(rollbackFor = Exception.class)
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
}
