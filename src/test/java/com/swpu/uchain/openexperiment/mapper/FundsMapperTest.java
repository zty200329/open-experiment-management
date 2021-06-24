package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.Funds;
import com.swpu.uchain.openexperiment.domain.MaxBigFunds;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import org.docx4j.wml.P;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FundsMapperTest {

    @Autowired
    private FundsMapper fundsMapper;
    @Autowired
    private KeyProjectStatusMapper keyProjectStatusMapper;

    @Autowired
    private ProjectGroupMapper projectGroupMapper;
    @Autowired
    private MaxBigFundsMapper maxBigFundsMapper;
    @Test
    public void multiInsert() {

        List<Funds> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Funds funds = new Funds();
            funds.setUse("测试原因");
            funds.setType(1);
            funds.setAmount(2314.12F);
            funds.setId((long) i);
            list.add(funds);
        }
        fundsMapper.multiInsert(list);
    }

    @Test
    public void multiInsert2() {


        List<Long> ids = keyProjectStatusMapper.getAllTest();
        for (Long id : ids) {
            ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(id);
            if(projectGroup.getApplyFunds() == 5000){
                MaxBigFunds maxBigFunds = maxBigFundsMapper.selectByCollege(String.valueOf(projectGroup.getSubordinateCollege()));
                if(maxBigFunds == null){
                    MaxBigFunds maxBigFunds1 = new MaxBigFunds();
                    maxBigFunds1.setNum(1);
                    maxBigFunds1.setCollege(String.valueOf(projectGroup.getSubordinateCollege()));
                    maxBigFundsMapper.insert(maxBigFunds1);
                }else{
                    maxBigFunds.setNum(maxBigFunds.getNum()+1);
                    maxBigFundsMapper.updateByPrimaryKey(maxBigFunds);
                }
            }
        }

    }
}