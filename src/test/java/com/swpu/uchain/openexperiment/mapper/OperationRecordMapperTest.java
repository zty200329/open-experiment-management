package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.DTO.TempDTO;
import com.swpu.uchain.openexperiment.DTO.TempDTO1;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.domain.UserProjectAccount;
import com.swpu.uchain.openexperiment.domain.UserRole;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.ProjectType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;


    @Test
    public void deleteByPrimaryKey() {
    }

    @Test

    public void insert1() {
        List<TempDTO1> dtos = userProjectAccountMapper.selectTemp1();
        for (TempDTO1 dto : dtos) {
//            if (dto.getCode().equals("201699010017")) {
//                User user = userMapper.selectByUserCode(dto.getCode());
//                User user1 = new User();
//                    user1.setCode(dto.getCode());
//                    user1.setInstitute(Integer.valueOf(dto.getCollege()));
//                    user1.setRealName(dto.getName());
//                    user1.setMobilePhone(dto.getTelephone());
//                userMapper.insert(user1);
//                System.out.println(dto+" "+user1);
//            }

                User user = userMapper.selectByUserCode(dto.getCode());
                UserRole userRole = userRoleMapper.selectByUserIdAndRoleId(Long.valueOf(dto.getCode()), 3);
                int i = 0;
                if (userRole == null || user == null) {
                    userMapper.deleteByCode(dto.getCode());
                    userRoleMapper.deleteByUserIdAndRoleId(Long.valueOf(dto.getCode()), 3L);
                    User user1 = new User();
                    user1.setCode(dto.getCode());
                    user1.setInstitute(Integer.valueOf(dto.getCollege()));
                    user1.setRealName(dto.getName());
                    user1.setMobilePhone(dto.getTelephone());
                    user1.setPassword("888888");
                    user1.setUserType(2);
                    if(dto.getSex()!=null) {
                        user1.setSex(String.valueOf(dto.getSex()));
                    }
                    userMapper.insert(user1);
                    if(dto.getCode().equals("201699010017")){
                      System.out.println("马柱");
                     }
                    UserRole userRole1 = new UserRole();
                    userRole1.setRoleId(3);
                    userRole1.setUserId(Long.valueOf(dto.getCode()));
                    userRoleMapper.insert(userRole1);
                    i++;
                }
            System.out.println(i);
            }


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
                if(dto.getRole() == 1){
                    userAccount.setUserType(2);
                }else {
                    userAccount.setUserType(1);
                }

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