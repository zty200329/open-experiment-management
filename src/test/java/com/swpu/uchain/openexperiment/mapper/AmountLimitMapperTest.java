package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.VO.limit.AmountAndTypeVO;
import com.swpu.uchain.openexperiment.enums.ProjectType;
import com.swpu.uchain.openexperiment.enums.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zty200329
 * @date 2020/10/4 10:56
 * @describe:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AmountLimitMapperTest {
    @Autowired
    private AmountLimitMapper amountLimitMapper;
    @Test
    public void test(){
        AmountAndTypeVO amountAndTypeVO = amountLimitMapper.getAmountAndTypeVOByCollegeAndProjectType(6, 2, 5);
        log.info(amountAndTypeVO.toString());
    }
}
