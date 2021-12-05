package cn.cruder.springtxinvalidation;

import cn.cruder.springtxinvalidation.constant.Constant;
import cn.cruder.springtxinvalidation.entity.AccountInfoEntity;
import cn.cruder.springtxinvalidation.mapper.AccountInfoMapper;
import cn.cruder.springtxinvalidation.service.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;

@Slf4j
@SpringBootTest
class ApplicationTests {
    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private AccountInfoServiceA accountInfoServiceA;
    @Autowired
    private AccountInfoServiceB accountInfoServiceB;
    @Autowired
    private AccountInfoServiceC accountInfoServiceC;
    @Autowired
    private AccountInfoServiceD accountInfoServiceD;
    @Autowired
    private AccountInfoServiceE accountInfoServiceE;

    @Autowired
    private AbstractPlatformTransactionManager abstractPlatformTransactionManager;

    @BeforeEach
    public void beforeEach() {
        // 查看用的是哪一个Manager
        log.info("TransactionManager:{}", String.valueOf(abstractPlatformTransactionManager.getClass()));
        initAmountValue(Constant.USER_1);
        initAmountValue(Constant.USER_2);
        log.info("BeforeEach Amount Info : \n{}", JSON.toJSONString(loadAmount()));
    }

    private void initAmountValue(Long id) {
        QueryWrapper<AccountInfoEntity> fromWrapper = new QueryWrapper<>();
        fromWrapper.eq("id", id);
        fromWrapper.last(" for update ");
        AccountInfoEntity infoEntity = accountInfoMapper.selectOne(fromWrapper);
        if (infoEntity == null) {
            infoEntity = AccountInfoEntity.builder().amount(Constant.DEFALUT_ACCOUNT_AMOUNT).build();
            // 手动set id
            infoEntity.setId(id);
            accountInfoMapper.insert(infoEntity);
        } else {
            infoEntity.setAmount(Constant.DEFALUT_ACCOUNT_AMOUNT);
            accountInfoMapper.updateById(infoEntity);
        }
    }

    List<AccountInfoEntity> loadAmount() {
        HashSet<Long> ids = new HashSet<>();
        ids.add(Constant.USER_2);
        ids.add(Constant.USER_1);
        return accountInfoMapper.selectBatchIds(ids);
    }

    @AfterEach
    void afterEach() {
        log.info("AfterEach Amount Info : \n{}", JSON.toJSONString(loadAmount()));
    }


    //------------------------------------------------------------------------------------------------------------------
    @Test
    void testServiceATransferA() throws FileNotFoundException {
        // commit
        accountInfoServiceA.transferA(Constant.USER_1, Constant.USER_2, 100);
    }

    @Test
    void testServiceATransferB() throws FileNotFoundException {
        // rollback
        accountInfoServiceA.transferB(Constant.USER_1, Constant.USER_2, 100);
    }


    //------------------------------------------------------------------------------------------------------------------

    @Test
    void testServiceBTransferA() throws FileNotFoundException {
        // commit
        accountInfoServiceB.transferA(Constant.USER_1, Constant.USER_2, 100);
    }

    @Test
    void testServiceBTransferB() throws FileNotFoundException {
        // rollback
        accountInfoServiceB.transferB(Constant.USER_1, Constant.USER_2, 100);
    }

    @Test
    void testServiceBTransferC() {
        // rollback
        accountInfoServiceB.transferC(Constant.USER_1, Constant.USER_2, 100);
    }


    //------------------------------------------------------------------------------------------------------------------
    @Test
    void testServiceCTransferA() throws FileNotFoundException {
        // AopExecutionTimeAspect 切面中提供了解决方案，但是已经注释
        // @Pointcut("execution(* cn.cruder.springtxinvalidation.service.AccountInfoServiceC.transferA(..))")
        // Aop的切入点包含改方法
        accountInfoServiceC.transferA(Constant.USER_1, Constant.USER_2, 100);
    }

    //------------------------------------------------------------------------------------------------------------------
    @Test
    void testServiceDTransferA() throws FileNotFoundException {
        accountInfoServiceD.transferA(Constant.USER_1, Constant.USER_2, 100);
    }


    //------------------------------------------------------------------------------------------------------------------

    @Test
    void testServiceETransferA1() throws InterruptedException {
        // 查看AbstractPlatformTransactionManager的实现 然后找到对应的doCommit方法 可以打断点实现，
        // 不过org.springframework.orm.jpa.JpaTransactionManager 没复现？？？
        // protected abstract void doCommit(DefaultTransactionStatus status) throws TransactionException;
        int threadNumber = 10;
        for (int i = 0; i < threadNumber; i++) {
            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    accountInfoServiceE.transferA(Constant.USER_1, Constant.USER_2, 500);
                }
            }, "n" + i).start();
        }
        Thread.sleep(10000);
    }


    @Test
    void testServiceETransferA2() {
        int threadNumber = 10;
        for (int i = 0; i < threadNumber; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (accountInfoServiceE) {
                        accountInfoServiceE.transferA(Constant.USER_1, Constant.USER_2, 1000);
                    }

                }
            }, "n" + i).start();
        }

    }


    @Test
    void testServiceETransferB() {
        int threadNumber = 10;
        for (int i = 0; i < threadNumber; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    accountInfoServiceE.transferB(Constant.USER_1, Constant.USER_2, 100);
                }
            }, "n" + i).start();
        }
    }

    @Test
    void testServiceETransferC() throws InterruptedException {
        int threadNumber = 10;

        for (int i = 0; i < threadNumber; i++) {
            new Thread(() -> accountInfoServiceE.transferC(Constant.USER_1, Constant.USER_2, 1000), "n" + i)
                    .start();
        }
        //Thread.sleep(10000);
    }

}
