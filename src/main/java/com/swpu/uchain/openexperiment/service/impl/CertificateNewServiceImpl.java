package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.domain.CertificateOpen;
import com.swpu.uchain.openexperiment.domain.NewCertificate;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.certificate.ApplyCertificate;
import com.swpu.uchain.openexperiment.mapper.CertificateOpenMapper;
import com.swpu.uchain.openexperiment.mapper.NewCertificateMapper;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.CertificateNewService;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zty
 * @date 2020/5/9 下午10:27
 * @description:
 */
@Service
@Slf4j
public class CertificateNewServiceImpl implements CertificateNewService {
    @Autowired
    private NewCertificateMapper newCertificateMapper;
    @Autowired
    private GetUserService getUserService;
    @Autowired
    private CertificateOpenMapper certificateOpenMapper;
    @Autowired
    private RedisUtil redisUtil;

    public boolean isOpen(){
        CertificateOpen certificateOpen = certificateOpenMapper.selectByPrimaryKey(1);
        Boolean isOpen = certificateOpen.getIsOpen();
        if(isOpen)
        {
            return true;
        }
        return false;
    }
    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public Result applyCertificate(ApplyCertificate applyCertificate) {
        Boolean isOpen = isOpen();
        if(!isOpen)
        {
            return Result.error(CodeMsg.SERVICE_NOT_ENABLED);
        }
        if(applyCertificate.getUserId().length()!=12){
            throw new GlobalException(CodeMsg.USER_ID_LENGTH);
        }
        NewCertificate newCertificate = new NewCertificate();
        BeanUtils.copyProperties(applyCertificate,newCertificate);
        newCertificate.setUserId(Long.valueOf(applyCertificate.getUserId()));
        if(applyCertificate.getProjectType()==1) {
            newCertificate.setProjectType("普通");
        }else {
            newCertificate.setProjectType("重点");
        }
        if(applyCertificate.getMemberRole() == 2){
            newCertificate.setMemberRole("项目组长");
        }else{
            newCertificate.setMemberRole("普通成员");
        }
        newCertificate.setIsTrue(true);
        newCertificateMapper.insert(newCertificate);
        return Result.success();
    }

    @Override
    public Result viewMyApplication() {
        User currentUser = getUserService.getCurrentUser();
        List<NewCertificate> newCertificates = newCertificateMapper.selectByUserId(Long.valueOf(currentUser.getCode()));
        return Result.success(newCertificates);
    }

    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public Result deleteMyApplication(Long[] id) {
        for (Long aLong : id) {
            newCertificateMapper.deleteByPrimaryKey(aLong);
        }

        return Result.success();
    }

    @Override
    public Result openApply() {
        if(isOpen())
        {
            return Result.error(CodeMsg.SERVICE_IS_OPEN);
        }
        CertificateOpen certificateOpen = certificateOpenMapper.selectByPrimaryKey(1);
        certificateOpen.setIsOpen(true);
        certificateOpenMapper.updateByPrimaryKey(certificateOpen);
        return Result.success();
    }

    @Override
    public Result closeApply() {
        if(!isOpen())
        {
            return Result.error(CodeMsg.SERVICE_NOT_ENABLED);
        }
        CertificateOpen certificateOpen = certificateOpenMapper.selectByPrimaryKey(1);
        certificateOpen.setIsOpen(false);
        certificateOpenMapper.updateByPrimaryKey(certificateOpen);
        return Result.success();
    }
}
