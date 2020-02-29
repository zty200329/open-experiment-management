package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.domain.Certificate;
import com.swpu.uchain.openexperiment.domain.CertificateOpen;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.mapper.CertificateMapper;
import com.swpu.uchain.openexperiment.mapper.CertificateOpenMapper;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.CertificateRequestService;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @Author zty
 * @Date 2020/2/28 下午2:45
 * @Description:
 */
@Slf4j
@Service
public class CertificateRequestServiceImpl implements CertificateRequestService {
    @Autowired
    private CertificateMapper certificateMapper;
    @Autowired
    private CertificateOpenMapper certificateOpenMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private GetUserService getUserService;

    @Override
    public Result judgeInterface() {

        CertificateOpen certificateOpen = certificateOpenMapper.selectByPrimaryKey(1);
        Boolean isOpen = certificateOpen.getIsOpen();
        if(!isOpen)
        {
           return Result.error(CodeMsg.SERVICE_NOT_ENABLED);
        }
        return Result.success();
    }

    public boolean isOpen(){
        CertificateOpen certificateOpen = certificateOpenMapper.selectByPrimaryKey(1);
        Boolean isOpen = certificateOpen.getIsOpen();
        if(isOpen)
        {
            return true;
        }
        return false;
    }

    /**
     * 拼接出所需要的集合
     * 返回具有申请资格的list集合
     * @return
     */

    public List<Certificate> selectByProjectStatus(Integer year){
        return certificateMapper.selectByProjectStatus(year+"%");
    }


    public List<Certificate> selectByUserId(String userId){
        return certificateMapper.selectByUserId(userId);
    }

    @Transactional(rollbackFor = GlobalException.class)
    public boolean openCertificateList(){
        CertificateOpen certificateOpen = new CertificateOpen();
        certificateOpen.setId(1);
        certificateOpen.setIsOpen(true);
        certificateOpenMapper.updateByPrimaryKey(certificateOpen);
        return true;
    }

    @Transactional(rollbackFor = GlobalException.class)
    public boolean closeCertificateList(){
        CertificateOpen certificateOpen = new CertificateOpen();
        certificateOpen.setId(1);
        certificateOpen.setIsOpen(false);
        certificateOpenMapper.updateByPrimaryKey(certificateOpen);
        return true;
    }

    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public Result getAllList(Integer year) {
        if(year == null){
            return Result.error(CodeMsg.INPUT_YEAR);
        }
        log.info("开启申请的项目批次："+year+"年启动的项目");
        /**
         * 判断是否已开启
         */
        Boolean isOpen = isOpen();
        if(isOpen)
        {
            return Result.error(CodeMsg.SERVICE_IS_OPEN);
        }

        //开启证书申领服务
        List<Certificate> certificates = selectByProjectStatus(year);
        for (Certificate certificate : certificates) {
            certificateMapper.insert(certificate);
        }

        if(!openCertificateList()){
            return Result.error(CodeMsg.OPEN_ERROR);
        }
        return Result.success();
    }

    public boolean deleteData(){
        int isDel = certificateMapper.deleteRequest();
        log.info(String.valueOf(isDel));
        if (isDel != 0){
            return true;
        }
        return false;
    }

    @Override
    public Result closeOpen() {
        if(!isOpen())
        {
            return Result.error(CodeMsg.SERVICE_NOT_ENABLED);
        }
        if(!deleteData()){
            return Result.error(CodeMsg.CLOSE_ERROR);
        }
        if(!closeCertificateList()){
            return Result.error(CodeMsg.CLOSE_ERROR);
        }
        return Result.success();
    }

    /**
     * 根据学号查出某位学生的可申请列表
     * @return
     */
    @Override
    public Result getOwnCertificate() {
        if(!isOpen())
        {
            return Result.error(CodeMsg.SERVICE_NOT_ENABLED);
        }
        User currentUser = getUserService.getCurrentUser();
        String key = "getOwnCertificate"+currentUser.getCode();

        if(redisUtil.get(key)!=null){
            log.info("----------------redis获取-----------------");
            List<Certificate> certificates = (List<Certificate>) redisUtil.get(key);
            return Result.success(certificates);
        }
        //查出is_need = 0的
        List<Certificate> certificates = selectByUserId(currentUser.getCode());

        redisUtil.set(key,certificates,180);
        return Result.success(certificates);
    }

    /**
     * 学生选择要申请证书的
     * @param primaryKey
     * @return
     */
    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public Result choseCertificate(Integer[] primaryKey) {
        if(primaryKey == null){
            throw new GlobalException(CodeMsg.CHOICE_IS_NULL);
        }
        for (Integer id : primaryKey) {
            Certificate certificate = certificateMapper.selectByPrimaryKey(id);
            certificate.setIsNeed(true);
            certificateMapper.updateByPrimaryKey(certificate);
            certificateMapper.insertFinally(certificate);
        }
        User currentUser = getUserService.getCurrentUser();
        String key = "getOwnCertificate"+currentUser.getCode();
        //由于提交了申请 该用户部分is_need发生改变 故删除redis
        redisUtil.del(key);
        return Result.success();
    }

    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public Result getFinalList(Integer year) {
        if(year == null){
            return Result.error(CodeMsg.INPUT_YEAR);
        }
        List<Certificate> certificates = certificateMapper.slecetFinalByYear(year+"%");

        return Result.success(certificates);
    }
}
