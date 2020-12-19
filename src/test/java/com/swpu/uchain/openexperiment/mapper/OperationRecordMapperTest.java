package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.DTO.TempDTO;
import com.swpu.uchain.openexperiment.domain.UserProjectAccount;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.ProjectType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.rmi.runtime.Log;

import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OperationRecordMapperTest {

    @Autowired
    private OperationRecordMapper recordMapper;

    @Autowired
    private UserProjectAccountMapper userProjectAccountMapper;


    @Test
    public void deleteByPrimaryKey() {
    }

    @Test
    public void insert() {
        List<TempDTO> dtos = userProjectAccountMapper.selectTemp();
        for (TempDTO dto : dtos) {
            //验证老师项目是否达到申请上限
            UserProjectAccount userProjectAccount = userProjectAccountMapper.selectByCode(dto.getId());
            //存在该用户记录
            if(userProjectAccount != null) {

                    if(dto.getType().equals(ProjectType.GENERAL.getValue())){
                        userProjectAccount.setGeneralNum(userProjectAccount.getGeneralNum()+1);
                    }else {
                        userProjectAccount.setKeyNum(userProjectAccount.getKeyNum()+1);
                    }
                    userProjectAccountMapper.updateByPrimaryKey(userProjectAccount);

                //不存在
            }else{
                UserProjectAccount userAccount = new UserProjectAccount();
                userAccount.setCode(dto.getId());
                userAccount.setCollege(dto.getCollege());
                userAccount.setUserType(dto.getType());
                if(dto.getType().equals(ProjectType.GENERAL.getValue())){
                    userAccount.setGeneralNum(1);
                    userAccount.setKeyNum(0);
                }else {
                    userAccount.setGeneralNum(0);
                    userAccount.setKeyNum(1);
                }
                userProjectAccountMapper.insert(userAccount);
            }
        }
    }

    @Test
    public void selectAllByProjectIdList() {

        List<Long> list = new LinkedList<>();
        list.add(1L);
        list.add(4L);
        System.err.println(recordMapper.selectAllByProjectIdList(list));
    }

    @Test
    public void setNotVisibleByProjectId() {
        recordMapper.setNotVisibleByProjectId(1L,1);
    }

    @Test
    public void updateByPrimaryKey() {
    }
}