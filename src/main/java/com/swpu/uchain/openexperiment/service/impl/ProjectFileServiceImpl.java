package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.AttachmentFileDTO;
import com.swpu.uchain.openexperiment.DTO.ConclusionDTO;
import com.swpu.uchain.openexperiment.VO.file.AttachmentFileVO;
import com.swpu.uchain.openexperiment.VO.project.ProjectTableInfo;
import com.swpu.uchain.openexperiment.config.UploadConfig;
import com.swpu.uchain.openexperiment.mapper.ProjectFileMapper;
import com.swpu.uchain.openexperiment.mapper.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.ProjectFile;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.FileType;
import com.swpu.uchain.openexperiment.enums.MaterialType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.FileKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.ProjectFileService;
import com.swpu.uchain.openexperiment.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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

    @Value(value = "${file.ip-address}")
    private String ipAddress;

    public boolean insert(ProjectFile projectFile) {
        ProjectFile projectFile1 = projectFileMapper.selectByProjectGroupIdAndMaterialType(projectFile.getProjectGroupId(), projectFile.getMaterialType());
        if (projectFile1 != null) {
            projectFile.setId(projectFile1.getId());
            return update(projectFile);
        }
        return projectFileMapper.insert(projectFile) == 1;
    }

    public boolean update(ProjectFile projectFile) {
        return projectFileMapper.updateByPrimaryKey(projectFile) == 1;
    }

    @Override
    public void delete(Long id) {
        ProjectFile projectFile = selectById(id);
        if (projectFile == null) {
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        redisService.delete(FileKey.getById, id + "");
        if (FileUtil.deleteFile(FileUtil.getFileRealPath(
                projectFile.getId(),
                uploadConfig.getApplyDir(),
                projectFile.getFileName()))) {
            projectFileMapper.deleteByPrimaryKey(id);
        }
        throw new GlobalException(CodeMsg.DELETE_FILE_ERROR);
    }

    @Override
    public ProjectFile selectById(Long id) {
        return projectFileMapper.selectByPrimaryKey(id);
    }

    @Override
    public ProjectFile getAimNameProjectFile(Long projectGroupId, String aimFileName) {
        return projectFileMapper.selectByGroupIdFileName(projectGroupId, aimFileName);
    }

    @Override
    public Result uploadApplyDoc(MultipartFile file,MultipartFile headFile, Long projectGroupId) {
        //先检查文件是否为空
        if (file == null || headFile == null) {
            throw new GlobalException(CodeMsg.UPLOAD_CANT_BE_EMPTY);
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
        String headDocPath = FileUtil.getFileRealPath(projectGroupId,
                uploadConfig.getApplyDir2(),
                uploadConfig.getApplyFileName() + ".doc");
        //如果存在则覆盖
        File dest = new File(bodyDocPath);
        dest.delete();



        if (!checkFileFormat(file, FileType.WORD.getValue())) {
            return Result.error(CodeMsg.FORMAT_UNSUPPORTED);
        }
        User user = getUserService.getCurrentUser();

        //TODO,校验当前用户是否有权进行上传
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

            try {
                DocumentTransformUtil.html2doc(new File(headDocPath), FileUtils.readFileToString(new File(headHtmlPath),"UTF-8"));
            } catch (IOException e) {
                throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
            }
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

            try {
                PDFConvertUtil.Word2Pdf(headDocPath,pdfHeadPath,bodyDocPath,pdfBodyPath);
            } catch (IOException e) {
                throw new GlobalException(CodeMsg.PDF_CONVERT_ERROR);
            }

            mergePdf(pdfHeadPath, pdfBodyPath, pdfPath);

            Map<String, String> map = new HashMap<>();
            map.put("url", ipAddress + "/apply/" + fileName);
            return Result.success(map);
        }
        return Result.error(CodeMsg.UPLOAD_ERROR);
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
    public Result uploadAttachmentFile(MultipartFile multipartFile, Integer attachmentType) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return Result.error(CodeMsg.UPLOAD_CANT_BE_EMPTY);
        }


        User currentUser = getUserService.getCurrentUser();
        ProjectFile projectFile = new ProjectFile();
        projectFile.setFileName(multipartFile.getOriginalFilename());
        projectFile.setDownloadTimes(0);
        projectFile.setFileType(FileUtil.getType(FileUtil.getMultipartFileSuffix(multipartFile)));
        projectFile.setSize(FileUtil.FormatFileSize(multipartFile.getSize()));
        projectFile.setUploadTime(new Date());
        projectFile.setMaterialType(MaterialType.APPLY_MATERIAL.getValue());
        projectFile.setUploadUserId(Long.valueOf(currentUser.getCode()));
        if (!insert(projectFile)) {
            return Result.error(CodeMsg.ADD_ERROR);
        }
        if (!FileUtil.uploadFile(
                multipartFile,
                FileUtil.getFileRealPath(
                        projectFile.getId(),
                        uploadConfig.getApplyDir(),
                        projectFile.getFileName()))) {
            return Result.error(CodeMsg.UPLOAD_ERROR);
        }
        return Result.success();
    }

    @Override
    public void downloadAttachmentFile(long fileId, HttpServletResponse response) {
        ProjectFile projectFile = selectById(fileId);
        if (projectFile == null) {
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        if (FileUtil.downloadFile(response, FileUtil.getFileRealPath(fileId, uploadConfig.getApplyDir(), projectFile.getFileName()))) {
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


        //判断是否存在该文件,若存在则进行覆盖
        ProjectFile projectFile = projectFileMapper.selectByProjectGroupIdAndMaterialType(projectId, MaterialType.CONCLUSION_MATERIAL.getValue());

        if (projectFile != null) {
            FileUtil.uploadFile(
                    file,
                    FileUtil.getFileRealPath(
                            projectFile.getId(),
                            uploadConfig.getConclusionDir(),
                            uploadConfig.getConcludingFileName()));
        }
        User currentUser = getUserService.getCurrentUser();

        //TODO,校验当前用户是否有权进行上传


        projectFile = new ProjectFile();
        projectFile.setUploadUserId(Long.valueOf(currentUser.getCode()));
        //数据库存储为pdf名称
        projectFile.setFileName(projectId + "_" + uploadConfig.getConcludingFileName() + ".pdf");
        projectFile.setUploadTime(new Date());
        projectFile.setMaterialType(MaterialType.CONCLUSION_MATERIAL.getValue());
        projectFile.setSize(FileUtil.FormatFileSize(file.getSize()));
        projectFile.setFileType(FileUtil.getType(FileUtil.getMultipartFileSuffix(file)));
        projectFile.setDownloadTimes(0);
        projectFile.setProjectGroupId(projectId);

        if (!insert(projectFile)) {
            return Result.error(CodeMsg.ADD_ERROR);
        }
        String docPath = FileUtil.getFileRealPath(projectId,
                uploadConfig.getConclusionDir(),
                uploadConfig.getConcludingFileName() + getFileSuffix(file.getOriginalFilename()));
        String pdfPath = FileUtil.getFileRealPath(projectId,
                uploadConfig.getConclusionDir(),
                uploadConfig.getConcludingFileName() + ".pdf");
        if (!FileUtil.uploadFile(
                file,
                docPath)) {
            return Result.error(CodeMsg.UPLOAD_ERROR);
        }
        // 异步转换成PDF
        convertDocToPDF(docPath, pdfPath);
        return Result.success();
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
    public void generateEstablishExcel(HttpServletResponse response) {

        User user = getUserService.getCurrentUser();
        //获取管理人员所管理的学院
        if (user == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        Integer college = user.getInstitute();
        if (college == null) {
            throw new GlobalException(CodeMsg.COLLEGE_TYPE_NULL_ERROR);
        }
        List<ProjectTableInfo> list = projectGroupMapper.getProjectTableInfoListByCollegeAndList(college);
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
        String[] head = {"院/中心", "序号", "项目名称", "实验类型", "实验时数", "指导教师", "负责学生"
                , "专业年级", "开始时间", "结束时间", "开放\r\n实验室", "实验室地点", "负责学生\r\n电话", "申请经费（元）", "建议\r\n评审分组"};
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
            //创建行
            // 创建行

            row = sheet.createRow(index++);

            //设置行高
            row.setHeight((short) (16 * 22));
            // 序号
            row.createCell(0).setCellValue(ConvertUtil.getStrCollege(projectTableInfo.getCollege()));
            row.createCell(1).setCellValue(projectTableInfo.getSerialNumber());
            row.createCell(2).setCellValue(projectTableInfo.getProjectName());
            row.createCell(3).setCellValue(ConvertUtil.getStrExperimentType(projectTableInfo.getExperimentType()));
            row.createCell(4).setCellValue(projectTableInfo.getTotalHours());
            row.createCell(5).setCellValue(projectTableInfo.getLeadTeacher());
            row.createCell(6).setCellValue(projectTableInfo.getLeadStudent());
            row.createCell(7).setCellValue(projectTableInfo.getGradeAndMajor());
            row.createCell(8).setCellValue(projectTableInfo.getStartTime());
            row.createCell(9).setCellValue(projectTableInfo.getEndTime());
            row.createCell(10).setCellValue(projectTableInfo.getLabName());
            row.createCell(11).setCellValue(projectTableInfo.getAddress());
            row.createCell(12).setCellValue(projectTableInfo.getLeadStudentPhone());
            row.createCell(13).setCellValue(projectTableInfo.getApplyFunds());
            row.createCell(14).setCellValue(projectTableInfo.getSuggestGroupType());

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
