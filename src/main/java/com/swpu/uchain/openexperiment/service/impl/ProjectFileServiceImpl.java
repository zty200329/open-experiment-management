package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.AttachmentFileDTO;
import com.swpu.uchain.openexperiment.DTO.ConclusionDTO;
import com.swpu.uchain.openexperiment.VO.file.AttachmentFileVO;
import com.swpu.uchain.openexperiment.VO.project.ProjectAnnex;
import com.swpu.uchain.openexperiment.VO.project.ProjectTableInfo;
import com.swpu.uchain.openexperiment.VO.project.UploadAttachmentFileVO;
import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import com.swpu.uchain.openexperiment.config.UploadConfig;
import com.swpu.uchain.openexperiment.domain.*;
import com.swpu.uchain.openexperiment.enums.*;
import com.swpu.uchain.openexperiment.mapper.*;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.FileKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.ProjectFileService;
import com.swpu.uchain.openexperiment.service.TimeLimitService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import com.swpu.uchain.openexperiment.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description
 * @Author cby
 * @Date 19-1-22
 **/
@Service
@Slf4j
@Component
public class ProjectFileServiceImpl implements ProjectFileService {
    @Autowired
    private UploadConfig uploadConfig;
    @Autowired
    private ProjectFileMapper projectFileMapper;
    @Autowired
    private GetUserService getUserService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ConvertUtil convertUtil;
    @Autowired
    private ProjectGroupMapper projectGroupMapper;
    @Autowired
    private KeyProjectStatusMapper keyProjectStatusMapper;

    @Autowired
    private UserProjectGroupMapper userProjectGroupMapper;
    @Autowired
    private UserProjectService userProjectService;
    @Autowired
    private TimeLimitService timeLimitService;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Value(value = "${file.ip-address}")
    private String ipAddress;

    /**
     * 多角色身份验证
     *
     * @param roleType 需要的角色
     * @return
     */
    private boolean validContainsUserRole(RoleType roleType) {
        User user = getUserService.getCurrentUser();
        //用户角色组
        List<UserRole> list = userRoleMapper.selectByUserId(Long.valueOf(user.getCode()));
        if (list == null || list.size() == 0) {
            throw new GlobalException(CodeMsg.PERMISSION_DENNY);
        }

        for (UserRole userRole : list
        ) {
            if (roleType.getValue().equals(userRole.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public boolean insert(ProjectFile projectFile) {

        //如果为申请只能存在一条 成果附件也只能有一条
        if (projectFile.getMaterialType() == 1 || projectFile.getMaterialType() == 11) {
            ProjectFile projectFile1 = projectFileMapper.selectByProjectGroupIdAndMaterialType(projectFile.getProjectGroupId(), projectFile.getMaterialType(), null);
            if (projectFile1 != null) {
                projectFile.setId(projectFile1.getId());
                return update(projectFile);
            }
        } else {
            ProjectFile projectFile1 = projectFileMapper.selectByProjectGroupIdAndFileName(projectFile.getProjectGroupId(), projectFile.getFileName());
            if (projectFile1 != null) {
                projectFile.setId(projectFile1.getId());
                return update(projectFile);
            }
        }
        return projectFileMapper.insert(projectFile) == 1;
    }

    public boolean update(ProjectFile projectFile) {
        return projectFileMapper.updateByPrimaryKey(projectFile) == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        //TODO 这要弄
        ProjectFile projectFile = selectById(id);
        if (projectFile == null) {
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        redisService.delete(FileKey.getById, id + "");
        //根据文件类型
        /**
         * 1 申请材料
         * 2 结题报告
         * 3 实验报告
         * 10 附件
         * 11 成果附件
         */
        Integer materialType = projectFile.getMaterialType();
        //不提供删除申请文件

        //删除结题报告或实验报告
        if(materialType == 2 || materialType == 3){
            String docPath = projectFile.getFileName().replace("pdf","doc");
            log.info(docPath);
             FileUtil.deleteFile(FileUtil.getFileRealPath(
                    uploadConfig.getConclusionDir(),
                    docPath));
            if (FileUtil.deleteFile(FileUtil.getFileRealPath(
                    uploadConfig.getConclusionPdf(),
                    projectFile.getFileName()))) {
                projectFileMapper.deleteByPrimaryKey(id);
            }
        }
        //附件
        else if(materialType == 10 ){
            if (FileUtil.deleteFile(FileUtil.getFileRealPath(
                    uploadConfig.getConclusionAnnex(),
                    projectFile.getFileName()))) {
                projectFileMapper.deleteByPrimaryKey(id);
            }
        }
        //成果附件
        else if(materialType == 11){
            if (FileUtil.deleteFile(FileUtil.getFileRealPath(
                    uploadConfig.getAchievementAnnex(),
                    projectFile.getFileName()))) {
                projectFileMapper.deleteByPrimaryKey(id);
            }
        }
        else {
            throw new GlobalException(CodeMsg.DELETE_FILE_ERROR);
        }
    }

    @Override
    public ProjectFile selectById(Long id) {
        return projectFileMapper.selectByPrimaryKey(id);
    }

    @Override
    public ProjectFile getAimNameProjectFile(Long projectGroupId, String aimFileName) {
        return projectFileMapper.selectByGroupIdFileName(projectGroupId, aimFileName);
    }

    /**
     * 重点项目申请文件
     * @param file
     * @param headFile
     * @param projectGroupId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result uploadApplyDoc(MultipartFile file, MultipartFile headFile, Long projectGroupId) {
        //先检查文件是否为空
        if (file == null || headFile == null) {
            throw new GlobalException(CodeMsg.UPLOAD_CANT_BE_EMPTY);
        }

        User user = getUserService.getCurrentUser();

        ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(projectGroupId);

        //如果是职能部门，不需要验证项目的状态
        if (validContainsUserRole(RoleType.FUNCTIONAL_DEPARTMENT)) {
            log.info("职能部门对进行项目编号为" + projectGroupId + "的项目进行正文修改");
            //验证项目状态合法性，为其中之一的状态均可以通过
        } else if (!projectGroup.getStatus().equals(ProjectStatus.LAB_ALLOWED.getValue()) &&
                !projectGroup.getStatus().equals(ProjectStatus.REJECT_MODIFY.getValue())) {
            if(keyProjectStatusMapper.getStatusByProjectId(projectGroupId) !=null){
                //判定是否为重点项目驳回状态,该状态可进行文件提交
                if(!keyProjectStatusMapper.getStatusByProjectId(projectGroupId).equals(ProjectStatus.TO_DE_CONFIRMED.getValue())
                        && !keyProjectStatusMapper.getStatusByProjectId(projectGroupId).equals(ProjectStatus.INTERIM_RETURN_MODIFICATION.getValue())){
                    throw new GlobalException(CodeMsg.PROJECT_CURRENT_STATUS_ERROR);
                }
            }else{
                throw new GlobalException(CodeMsg.PROJECT_CURRENT_STATUS_ERROR);
            }
        }

        if (!getFileSuffix(file.getOriginalFilename()).equals(".doc") || !getFileSuffix(headFile.getOriginalFilename()).equals(".html")) {
            throw new GlobalException(CodeMsg.FORMAT_UNSUPPORTED);
        }
        //重点项目申请正文
        String bodyDocPath = FileUtil.getFileRealPath(projectGroupId,
                uploadConfig.getApplyDir(),
                uploadConfig.getApplyFileName() + getFileSuffix(file.getOriginalFilename()));
        //项目基本信息html
        String headHtmlPath = FileUtil.getFileRealPath(projectGroupId,
                uploadConfig.getApplyDir2(),
                uploadConfig.getApplyFileName() + getFileSuffix(headFile.getOriginalFilename()));

        //项目基本信息doc路径
        //如果存在则覆盖
        File dest = new File(bodyDocPath);
        dest.delete();


        if (!checkFileFormat(file, FileType.WORD.getValue())) {
            return Result.error(CodeMsg.FORMAT_UNSUPPORTED);
        }

        //校验当前用户是否有权进行上传
        UserProjectGroup userProjectGroup = userProjectGroupMapper.selectByProjectGroupIdAndUserId(projectGroupId, Long.valueOf(user.getCode()));
        if (userProjectGroup == null || !userProjectGroup.getMemberRole().equals(MemberRole.PROJECT_GROUP_LEADER.getValue())) {
            int SubordinateCollege = projectGroupMapper.selectSubordinateCollege(projectGroupId);
            if (SubordinateCollege != 0) {
                throw new GlobalException(CodeMsg.PERMISSION_DENNY);
            }
        }
        ProjectFile projectFile = new ProjectFile();
        projectFile.setUploadUserId(Long.valueOf(user.getCode()));
        projectFile.setFileType(FileType.WORD.getValue());
        String fileName = projectGroupId + "_" + uploadConfig.getApplyFileName() + ".pdf";
        projectFile.setFileName(fileName);
        projectFile.setSize(FileUtil.FormatFileSize(file.getSize()));
        projectFile.setUploadTime(new Date());
        projectFile.setDownloadTimes(0);
        projectFile.setMaterialType(MaterialType.APPLY_MATERIAL.getValue());
        projectFile.setProjectGroupId(projectGroupId);
        if (!insert(projectFile)) {
            return Result.error(CodeMsg.ADD_ERROR);
        }
        //上传文件并转化成PDF
        if (FileUtil.uploadFile(file, bodyDocPath) && FileUtil.uploadFile(headFile, headHtmlPath)) {

            //直接将HTML转化为PDF
//            try {
//                DocumentTransformUtil.html2doc(new File(headDocPath), FileUtils.readFileToString(new File(headHtmlPath),"UTF-8"));
//            } catch (IOException e) {
//                throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
//            }
            //临时的PDF
            String pdfHeadPath = FileUtil.getFileRealPath(projectGroupId,
                    uploadConfig.getPdfTempDir(),
                    uploadConfig.getApplyFileName() + "head" + ".pdf");
            String pdfBodyPath = FileUtil.getFileRealPath(projectGroupId,
                    uploadConfig.getPdfTempDir(),
                    uploadConfig.getApplyFileName() + "body" + ".pdf");


            //转换为PDF
            //生成PDF的文件地址，该PDF信息是最终存入数据库的PDF名称
            String pdfPath = FileUtil.getFileRealPath(projectGroupId,
                    uploadConfig.getApplyDir(),
                    uploadConfig.getApplyFileName() + ".pdf");

            //转化为PDF
            try {
                Html2PDFUtil.convertHtml2PDF(headHtmlPath, pdfHeadPath);
                log.info("开始转化HTML头文件为PDF----------------");
                PDFConvertUtil.Word2Pdf(bodyDocPath, pdfBodyPath);
                log.info("开始内容正文文件为PDF----------------");
            } catch (IOException e) {
                throw new GlobalException(CodeMsg.PDF_CONVERT_ERROR);
            }

            log.info("开始合并PDF-------");
            mergePdf(pdfHeadPath, pdfBodyPath, pdfPath);

            Result result = deleteTempFile(projectGroupId);
            log.info(result.toString());
            Map<String, String> map = new HashMap<>();
            map.put("url", ipAddress + "/apply/" + fileName);
            return Result.success(map);
        }
        return Result.error(CodeMsg.UPLOAD_ERROR);
    }

    private Result deleteTempFile(Long projectId){
        String fileName1 = projectId + "_" + uploadConfig.getApplyFileName() + ".html";
        String fileName2 = projectId + "_" + uploadConfig.getApplyFileName()+"head" + ".pdf";
        String fileName3 = projectId + "_" + uploadConfig.getApplyFileName()+"body" + ".pdf";

        FileUtil.deleteFile(FileUtil.getFileRealPath(
                uploadConfig.getApplyDir2(),
                fileName1));
        log.info("删除1");
        FileUtil.deleteFile(FileUtil.getFileRealPath(
                uploadConfig.getPdfTempDir(),
                fileName2));
        log.info("");
        FileUtil.deleteFile(FileUtil.getFileRealPath(
                uploadConfig.getPdfTempDir(),
                fileName3));
        return Result.success();
    }
    @Override
    public void downloadApplyFile(Long fileId, HttpServletResponse response) {
        ProjectFile projectFile = projectFileMapper.selectByPrimaryKey(fileId);
        if (projectFile == null) {
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        String realPath = uploadConfig.getApplyDir() + '/' + projectFile.getFileName();
        if (FileUtil.downloadFile(response, realPath)) {
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
        }
        projectFile.setDownloadTimes(projectFile.getDownloadTimes() + 1);
        if (!update(projectFile)) {
            throw new GlobalException(CodeMsg.UPDATE_ERROR);
        }
    }

    @Override
    public void getConclusionDoc(Long fileId, HttpServletResponse response) {
        ProjectFile projectFile = projectFileMapper.selectByPrimaryKey(fileId);
        if (projectFile == null) {
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        String realPath = uploadConfig.getConclusionDir() + "/" + projectFile.getFileName();
        if (FileUtil.downloadFile(response, realPath)) {
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
        }
        projectFile.setDownloadTimes(projectFile.getDownloadTimes() + 1);
        if (!update(projectFile)) {
            throw new GlobalException(CodeMsg.UPDATE_ERROR);
        }
    }

    @Override
    public void downloadApplyPdf(Long fileId, HttpServletResponse response) {
        ProjectFile projectFile = projectFileMapper.selectByPrimaryKey(fileId);
        if (projectFile == null) {
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }

        String realPath = FileUtil.getFileRealPath(
                projectFile.getProjectGroupId(),
                uploadConfig.getApplyDir(),
                FileUtil.getFileNameWithoutSuffix(projectFile.getFileName()));
        File file = new File(realPath);
        if (file.exists()) {
            if (FileUtil.downloadFile(response, realPath)) {
                throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
            }
        }
        if (FileUtil.downloadFile(response, realPath)) {
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
        }
    }

    @Override
    public List<ProjectFile> getProjectAllFiles(Long projectGroupId) {
//        return projectFileMapper.selectByProjectGroupIdAndMaterialType(projectGroupId,null);
        return null;
    }

    @Override
    public Result uploadAttachmentFile(List<MultipartFile> multipartFile, Long projectId) {
        //验证时间限制
//        timeLimitService.validTime(TimeLimitType.UPLOADING_INFORMATION);
        if (multipartFile == null || multipartFile.size() == 0) {
            throw new GlobalException(CodeMsg.UPLOAD_CANT_BE_EMPTY);
        }
        User currentUser = getUserService.getCurrentUser();
        if (userProjectService.selectByProjectGroupIdAndUserId(projectId, Long.valueOf(currentUser.getCode())) == null) {
            return Result.error(CodeMsg.PERMISSION_DENNY);
        }

        List<ProjectAnnex> attachmentUrls = new ArrayList<>();
        for (MultipartFile file : multipartFile) {


            ProjectFile projectFile = new ProjectFile();
            projectFile.setProjectGroupId(projectId);
            projectFile.setFileName(projectId + "_附件_" + file.getOriginalFilename());
            projectFile.setDownloadTimes(0);
            projectFile.setFileType(FileUtil.getType(FileUtil.getMultipartFileSuffix(file)));
            log.info(projectFile.getFileType().toString());
            if (projectFile.getFileType() != 3 && projectFile.getFileType() != 4) {
                throw new GlobalException(CodeMsg.FORMAT_UNSUPPORTED);
            }
            projectFile.setSize(FileUtil.FormatFileSize(file.getSize()));
            projectFile.setUploadTime(new Date());
            projectFile.setMaterialType(MaterialType.ATTACHMENT_FILE.getValue());
            projectFile.setUploadUserId(Long.valueOf(currentUser.getCode()));
            if (!insert(projectFile)) {
                return Result.error(CodeMsg.ADD_ERROR);
            }
            if (!FileUtil.uploadFile(
                    file, uploadConfig.getConclusionAnnex() + "/" + projectFile.getFileName())) {
                return Result.error(CodeMsg.UPLOAD_ERROR);
            }
            ProjectAnnex projectAnnex = new ProjectAnnex();
            BeanUtils.copyProperties(projectFile,projectAnnex);
            projectAnnex.setUrl(ipAddress + "/conclusionAnnex/" + projectFile.getFileName());
            attachmentUrls.add(projectAnnex);
        }
        return Result.success(attachmentUrls);
    }

    /**
     * 下载附件
     *
     * @param fileId
     * @param response
     */
    @Override
    public void downloadAttachmentFile(long fileId, HttpServletResponse response) {
        ProjectFile projectFile = selectById(fileId);
        if (projectFile == null) {
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        if (FileUtil.downloadFile(response, FileUtil.getFileRealPath(uploadConfig.getConclusionAnnex(), projectFile.getFileName()))) {
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
        }
    }

    @Override
    public Result listAttachmentFiles() {
        List<AttachmentFileDTO> attachmentFileDTOS = projectFileMapper.selectAttachmentFiles();
        List<AttachmentFileVO> attachmentFileVOS = convertUtil.getAttachmentFileVOS(attachmentFileDTOS);
        return Result.success(attachmentFileVOS);
    }

    @Override
    public Result uploadConcludingReport(Long projectId, MultipartFile file) {
        ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(projectId);
        if (file == null) {
            throw new GlobalException(CodeMsg.FILE_EMPTY_ERROR);
        }

        if (projectGroup == null) {
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        if (!".doc".equals(getFileSuffix(file.getOriginalFilename()))) {
            throw new GlobalException(CodeMsg.FORMAT_UNSUPPORTED);
        }
        User currentUser = getUserService.getCurrentUser();


        if (userProjectService.selectByProjectGroupIdAndUserId(projectId, Long.valueOf(currentUser.getCode())) == null) {
            return Result.error(CodeMsg.PERMISSION_DENNY);
        }

        ProjectFile projectFile;
//                = projectFileMapper.selectByProjectGroupIdAndMaterialType(projectId, MaterialType.CONCLUSION_MATERIAL.getValue(), uploadConfig.getConcludingFileName());
//        //判断是否存在该文件,若存在则进行覆盖
//        if (projectFile != null) {
//            FileUtil.uploadFile(
//                    file,
//                    FileUtil.getFileRealPath(
//                            projectId,
//                            uploadConfig.getConclusionDir(),
//                            uploadConfig.getConcludingFileName() + getFileSuffix(file.getOriginalFilename())));
//        }

        projectFile = new ProjectFile();
        projectFile.setUploadUserId(Long.valueOf(currentUser.getCode()));
        //数据库存储为pdf名称
        projectFile.setFileName(projectId + "_" + uploadConfig.getConcludingFileName() + ".pdf");
        projectFile.setUploadTime(new Date());
        projectFile.setMaterialType(MaterialType.CONCLUSION_MATERIAL.getValue());
        projectFile.setSize(FileUtil.FormatFileSize(file.getSize()));
        projectFile.setFileType(FileUtil.getType(FileUtil.getMultipartFileSuffix(file)));
        if (projectFile.getFileType() != 2) {
            throw new GlobalException(CodeMsg.FORMAT_UNSUPPORTED);
        }
        projectFile.setDownloadTimes(0);
        projectFile.setProjectGroupId(projectId);
        String docPath = FileUtil.getFileRealPath(projectId,
                uploadConfig.getConclusionDir(),
                uploadConfig.getConcludingFileName() + getFileSuffix(file.getOriginalFilename()));
        String pdfPath = FileUtil.getFileRealPath(projectId,
                uploadConfig.getConclusionPdf(),
                uploadConfig.getConcludingFileName() + ".pdf");
        if (!FileUtil.uploadFile(
                file,
                docPath)) {
            return Result.error(CodeMsg.UPLOAD_ERROR);
        }
        if (!insert(projectFile)) {
            return Result.error(CodeMsg.ADD_ERROR);
        }
        // 异步转换成PDF
        convertDocToPDF(docPath, pdfPath);
        ProjectAnnex projectAnnex = new ProjectAnnex();
        BeanUtils.copyProperties(projectFile,projectAnnex);
        projectAnnex.setUrl(ipAddress + "/conclusion/" + projectFile.getFileName());
        return Result.success(projectAnnex);
    }

    @Override
    public Result uploadExperimentReport(Long projectId, MultipartFile file) {
        //TODO 项目状态
        ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(projectId);
        if (file == null) {
            throw new GlobalException(CodeMsg.FILE_EMPTY_ERROR);
        }

        if (projectGroup == null) {
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }

        if (!".doc".equals(getFileSuffix(file.getOriginalFilename()))) {
            throw new GlobalException(CodeMsg.FORMAT_UNSUPPORTED);
        }

        //判断是否存在该文件,若存在则进行覆盖
        ProjectFile projectFile;
//                = projectFileMapper.selectByProjectGroupIdAndMaterialType(projectId, MaterialType.EXPERIMENTAL_REPORT.getValue(), uploadConfig.getConcludingFileName());
//
//        if (projectFile != null) {
//            FileUtil.uploadFile(
//                    file,
//                    FileUtil.getFileRealPath(
//                            projectFile.getId(),
//                            uploadConfig.getConclusionDir(),
//                            uploadConfig.getExperimentReportFileName()));
//        }
        User currentUser = getUserService.getCurrentUser();

        //TODO,校验当前用户是否有权进行上传
        if (userProjectService.selectByProjectGroupIdAndUserId(projectId, Long.valueOf(currentUser.getCode())) == null) {
            return Result.error(CodeMsg.PERMISSION_DENNY);
        }

        projectFile = new ProjectFile();
        projectFile.setUploadUserId(Long.valueOf(currentUser.getCode()));
        //数据库存储为pdf名称
        projectFile.setFileName(projectId + "_" + uploadConfig.getExperimentReportFileName() + ".pdf");
        projectFile.setUploadTime(new Date());
        projectFile.setMaterialType(MaterialType.EXPERIMENTAL_REPORT.getValue());
        projectFile.setSize(FileUtil.FormatFileSize(file.getSize()));
        projectFile.setFileType(FileUtil.getType(FileUtil.getMultipartFileSuffix(file)));
        projectFile.setDownloadTimes(0);
        projectFile.setProjectGroupId(projectId);

        if (!insert(projectFile)) {
            return Result.error(CodeMsg.ADD_ERROR);
        }
        String docPath = FileUtil.getFileRealPath(projectId,
                uploadConfig.getConclusionDir(),
                uploadConfig.getExperimentReportFileName() + getFileSuffix(file.getOriginalFilename()));
        String pdfPath = FileUtil.getFileRealPath(projectId,
                uploadConfig.getConclusionPdf(),
                uploadConfig.getExperimentReportFileName() + ".pdf");
        if (!FileUtil.uploadFile(
                file,
                docPath)) {
            return Result.error(CodeMsg.UPLOAD_ERROR);
        }
        // 异步转换成PDF
        convertDocToPDF(docPath, pdfPath);
        ProjectAnnex projectAnnex = new ProjectAnnex();
        BeanUtils.copyProperties(projectFile,projectAnnex);
        projectAnnex.setUrl(ipAddress + "/conclusion/" + projectFile.getFileName());
        return Result.success(projectAnnex);
    }

    /**
     * 上传成果附件
     * @param projectGroupId
     * @param file
     * @return
     */
    @Override
    public Result uploadAchievementAnnex(Long projectGroupId, MultipartFile file) {
        if (file == null) {
            throw new GlobalException(CodeMsg.UPLOAD_CANT_BE_EMPTY);
        }
        User currentUser = getUserService.getCurrentUser();
        if (userProjectService.selectByProjectGroupIdAndUserId(projectGroupId, Long.valueOf(currentUser.getCode())) == null) {
            return Result.error(CodeMsg.PERMISSION_DENNY);
        }
        ProjectFile projectFile = new ProjectFile();
        projectFile.setProjectGroupId(projectGroupId);
        projectFile.setFileName(projectGroupId + "_重点项目成果附件.zip");
        projectFile.setDownloadTimes(0);
        projectFile.setFileType(FileUtil.getType(FileUtil.getMultipartFileSuffix(file)));
        if (projectFile.getFileType() != 5) {
            throw new GlobalException(CodeMsg.FORMAT_UNSUPPORTED);
        }
        projectFile.setSize(FileUtil.FormatFileSize(file.getSize()));
        projectFile.setUploadTime(new Date());
        projectFile.setMaterialType(MaterialType.ACHIEVEMENT_ANNEX.getValue());
        projectFile.setUploadUserId(Long.valueOf(currentUser.getCode()));
        if (!insert(projectFile)) {
            return Result.error(CodeMsg.ADD_ERROR);
        }
        if (!FileUtil.uploadFile(
                file, uploadConfig.getAchievementAnnex() + "/" + projectFile.getFileName())) {
            return Result.error(CodeMsg.UPLOAD_ERROR);
        }
        ProjectAnnex projectAnnex = new ProjectAnnex();
        BeanUtils.copyProperties(projectFile,projectAnnex);
        return Result.success(projectAnnex);
    }

    public boolean checkFileFormat(MultipartFile multipartFile, Integer aimType) {
        String suffix = FileUtil.getMultipartFileSuffix(multipartFile);
        int type = FileUtil.getType(suffix);
        if (type != aimType) {
            return false;
        }
        return true;
    }

    private int getYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }


    @Override
    public void generateEstablishExcel(HttpServletResponse response, Integer projectStatus) {

        User user = getUserService.getCurrentUser();
        //获取管理人员所管理的学院
        if (user == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        Integer college = user.getInstitute();
        List<ProjectTableInfo> list = projectGroupMapper.getProjectTableInfoListByCollegeAndList(college, projectStatus);
        // 1.创建HSSFWorkbook，一个HSSFWorkbook对应一个Excel文件
        XSSFWorkbook wb = new XSSFWorkbook();
        // 2.在workbook中添加一个sheet,对应Excel文件中的sheet(工作栏)
        XSSFSheet sheet = wb.createSheet("workSheet");

        sheet.setPrintGridlines(true);
        //3.1设置字体居中
        XSSFCellStyle cellStyle = wb.createCellStyle();
        //自动换行
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //当前行的位置
        int index = 0;

        //序号
        XSSFRow title = sheet.createRow(index);
        sheet.setColumnWidth(0, 256 * 150);
        title.setHeight((short) (16 * 50));
        title.createCell(index++).setCellValue("西南石油大学第" + getYear() / 100 + "期(" + getYear() + "-" + (getYear() + 1) + "年度)课外开放实验项目立项一览表");

        XSSFRow info = sheet.createRow(index);
        info.createCell(0).setCellValue("单位：（盖章）");
        sheet.setColumnWidth(0, 256 * 20);
        info.createCell(3).setCellValue("填报时间");
        sheet.setColumnWidth(index, 256 * 20);
        index++;

        // 4.设置表头，即每个列的列名
        String[] head = {"院/中心", "创建编号", "项目名称", "实验类型", "实验时数", "指导教师", "学生"
                , "专业年级", "开始时间", "结束时间", "开放\r\n实验室", "实验室地点", "负责学生姓名", "负责学生\r\n电话"
                , "申请经费（元）", "建议\r\n评审分组", "项目状态", "上报编号", "项目类型"};
        // 4.1创建表头行
        XSSFRow row = sheet.createRow(index++);

        //创建行中的列
        for (int i = 0; i < head.length; i++) {

            // 给列写入数据,创建单元格，写入数据
            row.setHeight((short) (16 * 40));
            row.createCell(i).setCellValue(head[i]);

        }

        //写入数据
        for (ProjectTableInfo projectTableInfo : list) {

            //替换项目状态(如果项目是重点,替换项目状态)
            if (projectTableInfo.getKeyProjectStatus() != null) {
                projectTableInfo.setProjectStatus(projectTableInfo.getKeyProjectStatus());
            }


            //项目组组长
            List<UserMemberVO> userMemberVOList =
                    userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(MemberRole.PROJECT_GROUP_LEADER.getValue(), projectTableInfo.getId(), JoinStatus.JOINED.getValue());
            StringBuilder students = new StringBuilder("");
            StringBuilder studentsMajorAndGrade = new StringBuilder();
            StringBuilder leaderName = new StringBuilder();
            StringBuilder leaderPhone = new StringBuilder();
            StringBuilder guideTeachers = new StringBuilder();
            for (int i = 0; i < userMemberVOList.size(); i++) {
                UserMemberVO userMemberVO = userMemberVOList.get(i);
                leaderName.append(userMemberVO.getUserName());
                if (userMemberVO.getPhone() != null) {
                    leaderPhone.append(userMemberVO.getPhone());
                }

                students.append(userMemberVO.getUserName());
                students.append("\r\n ");
                studentsMajorAndGrade.append(ConvertUtil.getGradeAndMajorByNumber(userMemberVO.getGrade() + userMemberVO.getMajor()));
                if (i != userMemberVOList.size() - 1) {
                    studentsMajorAndGrade.append("\r\n ");
                }
            }


            //项目成员
            List<UserMemberVO> userMemberVOList2 =
                    userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(MemberRole.NORMAL_MEMBER.getValue(), projectTableInfo.getId(), JoinStatus.JOINED.getValue());
            for (int i = 0; i < userMemberVOList2.size(); i++) {
                UserMemberVO userMemberVO = userMemberVOList2.get(i);
                students.append(userMemberVO.getUserName());
                students.append("\r\n ");
                studentsMajorAndGrade.append(ConvertUtil.getGradeAndMajorByNumber(userMemberVO.getGrade() + userMemberVO.getMajor()));
                if (i != userMemberVOList2.size() - 1) {
                    studentsMajorAndGrade.append("\r\n ");
                }
            }

            //指导教师
            List<UserMemberVO> userMemberVOList3 =
                    userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(MemberRole.GUIDANCE_TEACHER.getValue(), projectTableInfo.getId(), JoinStatus.JOINED.getValue());
            for (int i = 0; i < userMemberVOList3.size(); i++) {
                UserMemberVO userMemberVO = userMemberVOList3.get(i);
                guideTeachers.append(userMemberVO.getUserName());
                guideTeachers.append("\r\n ");
                if (i != userMemberVOList3.size() - 1) {
                    studentsMajorAndGrade.append("\r\n ");
                }
            }


            //创建行
            row = sheet.createRow(index++);

            //设置行高
            row.setHeight((short) (16 * 22));
            // 序号
            row.createCell(0).setCellValue(ConvertUtil.getStrCollege(projectTableInfo.getCollege()));
            if (projectTableInfo.getTempSerialNumber() != null) {
                row.createCell(1).setCellValue(projectTableInfo.getTempSerialNumber() + "T");
            }
            //项目名称
            row.createCell(2).setCellValue(projectTableInfo.getProjectName());
            //实验类型
            row.createCell(3).setCellValue(ConvertUtil.getStrExperimentType(projectTableInfo.getExperimentType()));

            row.createCell(4).setCellValue(projectTableInfo.getTotalHours());
            row.createCell(5).setCellValue(guideTeachers.toString());
            row.createCell(6).setCellValue(students.toString());
            row.createCell(7).setCellValue(studentsMajorAndGrade.toString());
            row.createCell(8).setCellValue(projectTableInfo.getStartTime().substring(0, 10));
            row.createCell(9).setCellValue(projectTableInfo.getEndTime().substring(0, 10));
            row.createCell(10).setCellValue(projectTableInfo.getLabName());
            row.createCell(11).setCellValue(projectTableInfo.getAddress());
            row.createCell(12).setCellValue(leaderName.toString());
            row.createCell(13).setCellValue(leaderPhone.length() == 0 ? "" : leaderPhone.toString());
            row.createCell(14).setCellValue(projectTableInfo.getApplyFunds());
            row.createCell(15).setCellValue(ConvertUtil.getStringSuggestGroupType(projectTableInfo.getSuggestGroupType()));
            row.createCell(16).setCellValue(projectTableInfo.getProjectStatus());
            row.createCell(17).setCellValue(projectTableInfo.getSerialNumber());
            row.createCell(18).setCellValue(ConvertUtil.getStrProjectType(projectTableInfo.getProjectType()));

        }

        sheet.createRow(index++).createCell(0).setCellValue("注1：本表由学院（中心）汇总填报。注2：建议评审分组填A-F,数据来源立项申请表");
        index++;

        XSSFRow end = sheet.createRow(index);
        end.createCell(0).setCellValue("主管院长签字:");
        end.createCell(3).setCellValue("经办人");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + "EstablishExcel" + ".xlsx");
        try {
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
        }
    }

    @Override
    public void generateConclusionExcel(HttpServletResponse response) {
        User user = getUserService.getCurrentUser();
        Integer college = user.getInstitute();
        // TODO  区分学院
        // 1.创建HSSFWorkbook，一个HSSFWorkbook对应一个Excel文件
        XSSFWorkbook wb = new XSSFWorkbook();
        // 2.在workbook中添加一个sheet,对应Excel文件中的sheet(工作栏)
        XSSFSheet sheet = wb.createSheet("workSheet");

        sheet.setPrintGridlines(true);
        //3.1设置字体居中
        XSSFCellStyle cellStyle = wb.createCellStyle();
        //自动换行
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //当前行的位置
        int index = 0;
        //序号
        XSSFRow title = sheet.createRow(index);
        sheet.setColumnWidth(0, 256 * 150);
        title.setHeight((short) (16 * 50));
        title.createCell(index++).setCellValue("西南石油大学第" + getYear() / 100 + "期（" + getYear() + "-" + (getYear() + 1) + "年度）课外开放实验普通项目结题验收一览表");


        XSSFRow info = sheet.createRow(index);
        sheet.setColumnWidth(0, 256 * 40);
        info.createCell(0).setCellValue("单位：（盖章）");


        // 4.1创建表头行
        XSSFRow row = sheet.createRow(index++);
        String[] columns = {"序号", "学院", "开放实验室", "项目编号", "实验类型", "实验时数"
                , "指导教师", "教师公号", "学生姓名", "学生学号", "组员职责", "专业年级", "起止时间", "验收时间", "验收结果", "备注"};
        //创建行中的列
        sheet.setColumnWidth(0, 256 * 20);
        for (int i = 0; i < columns.length; i++) {

            // 给列写入数据,创建单元格，写入数据
            row.setHeight((short) (16 * 40));
            row.createCell(i).setCellValue(columns[i]);
        }

        //写入数据
        List<ConclusionDTO> list = projectGroupMapper.selectConclusionDTOs(college);
        for (ConclusionDTO conclusion : list
        ) {
            // 创建行
            row = sheet.createRow(++index);

            //设置行高
            row.setHeight((short) (16 * 22));
            // 序号
            row.createCell(1).setCellValue(ConvertUtil.getStrCollege(conclusion.getCollege()));
            row.createCell(2).setCellValue(conclusion.getLabName());
            row.createCell(3).setCellValue(conclusion.getId());
            row.createCell(4).setCellValue(ConvertUtil.getStrExperimentType(conclusion.getExperimentType()));
            row.createCell(5).setCellValue(conclusion.getTotalHours());
            row.createCell(6).setCellValue(conclusion.getGuideTeacherName());
            row.createCell(7).setCellValue(conclusion.getGuideTeacherId());
            row.createCell(8).setCellValue(conclusion.getUserName());
            row.createCell(9).setCellValue(conclusion.getUserId());
            row.createCell(10).setCellValue(ConvertUtil.getStrMemberRole(conclusion.getUserRole()));
            row.createCell(11).setCellValue(conclusion.getMajorAndGrade());
            row.createCell(12).setCellValue(conclusion.getStartTimeAndEndTime());

        }

        sheet.createRow(++index).createCell(0).setCellValue("注1：本表由学院（中心）汇总填报。注2：建议评审分组填A-F,数据来源立项申请表");
        index++;

        XSSFRow end = sheet.createRow(index);
        end.createCell(0).setCellValue("主管院长签字:");
        end.createCell(3).setCellValue("经办人");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + "Conclusion" + ".xlsx");
        try {
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
        }

    }

    private void convertDocToPDF(String fileNameOfDoc, String fileNameOfPDF) {
        PDFConvertUtil.convert(fileNameOfDoc, fileNameOfPDF);
    }

    private void mergePdf(String headDocPath, String docPath, String pdfName) {


        String[] docs = new String[2];
        docs[0] = headDocPath;
        docs[1] = docPath;
        PDFMerge.mergePdfFiles(docs, pdfName);
    }

    /**
     * 解决IE edge 文件上传 文件名却出现了全路径+文件名的形式
     *
     * @param fileName 文件名
     * @return
     */
    private static String getFileSuffix(String fileName) {
        if (fileName == null) {
            throw new GlobalException(CodeMsg.FILE_NAME_EMPTY_ERROR);
        }
        //最后一位  注意是"\\",主要针对于微软的浏览器
        int lastIndexOfSlash = fileName.lastIndexOf(".");
        return fileName.substring(lastIndexOfSlash);
    }

}
