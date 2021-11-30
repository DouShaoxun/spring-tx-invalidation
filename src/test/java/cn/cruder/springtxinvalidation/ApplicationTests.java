package cn.cruder.springtxinvalidation;

import cn.cruder.springtxinvalidation.constant.Constant;
import cn.cruder.springtxinvalidation.entity.AccountInfoEntity;
import cn.cruder.springtxinvalidation.mapper.AccountInfoMapper;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;

@Slf4j
@SpringBootTest
class ApplicationTests {
    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @BeforeEach
    public void beforeEach() {
        initAmountValue(Constant.USER_1);
        initAmountValue(Constant.USER_2);
        log.info("BeforeEach  Amount Info = \n{}", JSON.toJSONString(loadAmount()));
    }

    private void initAmountValue(Long id) {
        AccountInfoEntity infoEntity = accountInfoMapper.selectById(id);
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
        log.info("AfterEach  Amount Info = \n{}", JSON.toJSONString(loadAmount()));
    }


    @Test
    void contextLoads() {
        System.out.println();
    }

}
