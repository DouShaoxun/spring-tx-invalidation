package cn.cruder.springtxinvalidation.service;

import cn.cruder.springtxinvalidation.constant.Constant;
import cn.cruder.springtxinvalidation.entity.AccountInfoEntity;
import cn.cruder.springtxinvalidation.mapper.AccountInfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.io.FileNotFoundException;

/**
 * @Author: cruder
 * @Date: 2021/12/01/21:44
 */
@Slf4j
@Service
@AllArgsConstructor
public class AccountInfoServiceB {

    private final AccountInfoMapper accountInfoMapper;

    /**
     * 指定rollbackFor = Exception.class了。
     * <br/>
     * 但是捕获了异常没处理
     *
     * @param from
     * @param to
     * @param amount
     * @throws FileNotFoundException
     */
    @Transactional(rollbackFor = Exception.class)
    public void transferA(Long from, Long to, Integer amount) throws FileNotFoundException {
        try {
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
        } catch (FileNotFoundException e) {
            log.error("transferA error", e);
        }
    }


    /**
     * cache
     *
     * @param from
     * @param to
     * @param amount
     * @throws FileNotFoundException
     */
    @Transactional(rollbackFor = Exception.class)
    public void transferB(Long from, Long to, Integer amount) throws FileNotFoundException {
        try {
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
        } catch (FileNotFoundException e) {
            log.error("transferB error", e);
            throw e;
        }
    }

    /**
     * cache
     *
     * @param from
     * @param to
     * @param amount
     * @throws FileNotFoundException
     */
    @Transactional(rollbackFor = Exception.class)
    public void transferC(Long from, Long to, Integer amount) {
        try {
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
        } catch (FileNotFoundException e) {
            log.error("transferC error", e);
            // 跟throw e;效果一样
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();

        }
    }

}
