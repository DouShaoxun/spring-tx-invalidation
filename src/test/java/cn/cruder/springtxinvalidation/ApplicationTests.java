package cn.cruder.springtxinvalidation;

import cn.cruder.springtxinvalidation.entity.AccountInfoEntity;
import cn.cruder.springtxinvalidation.mapper.AccountInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ApplicationTests {

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Test
    void contextLoads() {
        AccountInfoEntity accountInfoEntity = new AccountInfoEntity();
        accountInfoEntity.setAccount(100);
        accountInfoMapper.insert(accountInfoEntity);
    }

}
