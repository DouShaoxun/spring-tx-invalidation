package cn.cruder.springtxinvalidation.service;

import cn.cruder.springtxinvalidation.constant.Constant;
import cn.cruder.springtxinvalidation.entity.AccountInfoEntity;
import cn.cruder.springtxinvalidation.mapper.AccountInfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;

/**
 * @Author: cruder
 * @Date: 2021/12/02/21:51
 */
@Slf4j
@Service
@AllArgsConstructor
public class AccountInfoServiceD {
    private final AccountInfoMapper accountInfoMapper;


    /**
     * 调用本类的方法，没有使用代理对象
     * <br/>
     *
     * @param from
     * @param to
     * @param amount
     * @throws FileNotFoundException
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void transferA(Long from, Long to, Integer amount) throws FileNotFoundException {
        // 这里通过this调不会走代理
        // TODO: 2021-12-02 SpringBoot 2.6.1没有复现 但是不推荐用this调有切面的方法
        //System.out.println(this.getClass());
        //transferB(from, to, amount);
        // 解决方法 拿到代理对象 需要在启动类设置 @EnableAspectJAutoProxy(exposeProxy = true)
        AccountInfoServiceD accountInfoServiceD = (AccountInfoServiceD) AopContext.currentProxy();
        accountInfoServiceD.transferB(from, to, amount);

    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
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
