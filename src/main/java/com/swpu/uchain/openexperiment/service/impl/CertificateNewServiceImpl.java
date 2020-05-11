package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.domain.Certificate;
import com.swpu.uchain.openexperiment.domain.CertificateOpen;
import com.swpu.uchain.openexperiment.domain.NewCertificate;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.certificate.ApplyCertificateForm;
import com.swpu.uchain.openexperiment.form.certificate.DeleteCertificateForm;
import com.swpu.uchain.openexperiment.mapper.CertificateOpenMapper;
import com.swpu.uchain.openexperiment.mapper.NewCertificateMapper;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.CertificateNewService;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.util.FileUtil;
import com.swpu.uchain.openexperiment.util.RedisUtil;
import com.swpu.uchain.openexperiment.util.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${download.certificate-excel-download}")
    private String excelExportPath;

    public boolean isOpen() {
        CertificateOpen certificateOpen = certificateOpenMapper.selectByPrimaryKey(1);
        Boolean isOpen = certificateOpen.getIsOpen();
        if (isOpen) {
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public Result applyCertificate(ApplyCertificateForm applyCertificate) {
        Boolean isOpen = isOpen();
        if (!isOpen) {
            return Result.error(CodeMsg.SERVICE_NOT_ENABLED);
        }
        if (applyCertificate.getUserId().length() != 12) {
            throw new GlobalException(CodeMsg.USER_ID_LENGTH);
        }
        NewCertificate newCertificate = new NewCertificate();
        BeanUtils.copyProperties(applyCertificate, newCertificate);
        newCertificate.setUserId(Long.valueOf(applyCertificate.getUserId()));
        if (applyCertificate.getProjectType() == 1) {
            newCertificate.setProjectType("普通");
        } else {
            newCertificate.setProjectType("重点");
        }
        if (applyCertificate.getMemberRole() == 2) {
            newCertificate.setMemberRole("项目组长");
        } else {
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
    public Result deleteMyApplication(DeleteCertificateForm deleteCertificate) {


        newCertificateMapper.deleteByPrimaryKey(deleteCertificate.getId());

        return Result.success();
    }

    @Override
    public Result openApply() {
        if (isOpen()) {
            return Result.error(CodeMsg.SERVICE_IS_OPEN);
        }
        if (isTableEmpty()) {
            throw new GlobalException(CodeMsg.TABLE_IS_NOT_EMPTY);
        }
        CertificateOpen certificateOpen = certificateOpenMapper.selectByPrimaryKey(1);
        certificateOpen.setIsOpen(true);
        certificateOpenMapper.updateByPrimaryKey(certificateOpen);
        return Result.success();
    }

    @Override
    public Result closeApply() {
        if (!isOpen()) {
            return Result.error(CodeMsg.SERVICE_NOT_ENABLED);
        }
        CertificateOpen certificateOpen = certificateOpenMapper.selectByPrimaryKey(1);
        certificateOpen.setIsOpen(false);
        certificateOpenMapper.updateByPrimaryKey(certificateOpen);
        return Result.success();
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date());
    }

    public boolean createExcel() throws FileNotFoundException {
        List<NewCertificate> list = newCertificateMapper.selectAll();
        Map<String, String> map = new HashMap<String, String>();
        map.put("title", "开放性实验结题证书申报名单表");
        map.put("total", list.size() + " 条");
        map.put("date", getDate());

        ExcelUtil.getInstance().exportObj2ExcelByTemplate(map, "demo.xls",
                new FileOutputStream(excelExportPath + "/" + "年度名单.xls"),
                list, NewCertificate.class, true);
        return true;
    }

    @Override
    public Result downloadList(HttpServletResponse response) {
        try {
            boolean isDownload = createExcel();
            if (!isDownload) {
                throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        log.info("下载路径：" + excelExportPath);
        String fileUrl = excelExportPath;
        String fileName = "年度名单.xls";
        String realPath = fileUrl + "/" + fileName;

        if (FileUtil.downloadFile(response, realPath)) {
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
        }

        return Result.success();
    }

    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public Result emptyTheTable() {
        if (isOpen()) {
            return Result.error(CodeMsg.CANNOT_BE_CLEARED_WHILE_THE_SYSTEM_IS_OPEN);
        }
        newCertificateMapper.truncateTable();
        return Result.success();
    }

    private Boolean isTableEmpty() {
        List<NewCertificate> newCertificates = newCertificateMapper.selectAll();
        return newCertificates.size() != 0;
    }
}
