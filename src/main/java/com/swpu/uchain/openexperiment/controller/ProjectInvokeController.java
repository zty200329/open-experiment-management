package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-21
 * @Description:
 * 项目操作接口
 */
@CrossOrigin
@RestController
@RequestMapping("/project")
@Api(tags = "项目模块执行接口")
public class ProjectInvokeController {

    private ProjectService projectService;

    private UserProjectService userProjectService;

    @Autowired
    public ProjectInvokeController(ProjectService projectService, UserProjectService userProjectService) {
        this.projectService = projectService;
        this.userProjectService = userProjectService;
    }

    @ApiOperation("申请立项接口--可使用")
    @PostMapping(value = "/createApply", name = "申请立项接口")
    public Result createApply(@Valid @RequestBody CreateProjectApplyForm createProjectApplyForm){
        return projectService.applyCreateProject(createProjectApplyForm);
    }

    @ApiOperation("修改立项申请--可使用")
    @PostMapping(value = "/updateApply", name = "修改立项申请")
    public Result updateApply(@Valid @RequestBody UpdateProjectApplyForm updateProjectApplyForm){
        return projectService.applyUpdateProject(updateProjectApplyForm);
    }

    @ApiOperation("确认或者否认职能部门修改立项申请的结果(指导老师进行确认)1,确认(立项成功)  2.否认(立项失败)--可使用")
    @PostMapping(value = "/ensureOrNotModify", name = "修改立项申请")
    public Result ensureOrNotModify(@Valid @RequestBody ConfirmForm confirmForm){
        return projectService.ensureOrNotModify(confirmForm);
    }

    @ApiOperation("申请参与项目接口--可使用")
    @PostMapping(value = "/joinApply", name = "申请参与项目接口")
    public Result joinApply(@Valid @RequestBody JoinProjectApplyForm joinProjectApplyForm){
        return userProjectService.applyJoinProject(joinProjectApplyForm);
    }

    @ApiOperation("同意加入项目")
    @PostMapping(value = "/agreeJoin--可使用，如果在此处添加就无须再项目更新表单中添加学生", name = "同意加入项目")
    public Result agreeJoin(@Valid @RequestBody JoinForm[] joinForm){
        return projectService.agreeJoin(joinForm);
    }

    @ApiOperation("拒绝某用户加入项目组--可使用")
    @PostMapping(value = "/rejectJoin", name = "拒绝某用户加入项目组")
    public Result rejectJoin(@Valid @RequestBody JoinForm[] joinForm){
        return projectService.rejectJoin(joinForm);
    }

    @ApiOperation("同意立项(职能部门)--可使用")
    @PostMapping(value = "/agreeEstablish", name = "同意立项")
    public Result agreeEstablish(@Valid @RequestBody List<ProjectCheckForm> projectGroupIdList){
        return projectService.agreeEstablish(projectGroupIdList);
    }



    @ApiOperation("修改项目组成员身份  1.指导教师2.项目组长3.普通成员--可使用")
    @PostMapping(value = "/aimMemberLeader", name = "修改项目组成员身份-")
    public Result aimMemberLeader(@Valid @RequestBody AimForm aimForm){
        return userProjectService.aimUserMemberRole(aimForm);
    }


    @ApiOperation("追加立项申请内容(资金申请报账)--可使用")
    @PostMapping(value = "/appendCreateApply", name = "追加立项申请内容")
    public Result appendCreateApply(@Valid @RequestBody AppendApplyForm appendApplyForm){
        return projectService.appendCreateApply(appendApplyForm);
    }

    @ApiOperation("生成结题总览表--待完成")
    @GetMapping(value = "generateConclusionExcel", name = "生成结题总览表")
    public void generateConclusionExcel(){
        projectService.generateConclusionExcel();
    }

    @ApiOperation("生成立项总览表--待完成")
    @GetMapping(value = "generateEstablishExcel", name = "生成立项总览表")
    public void generateEstablishExcel(){
        projectService.generateEstablishExcel();
    }

    @ApiOperation("上报学院领导(二级单位)--可使用")
    @PostMapping(value = "/reportToCollegeLeader", name = "上报学院领导")
    public Result reportToCollegeLeader(Long projectGroupId){
        if (projectGroupId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return projectService.reportToCollegeLeader(projectGroupId);
    }


    @ApiOperation("上报职能部门--可使用")
    @PostMapping(value = "/reportToFunctionalDepartment")
    public Result reportToFunctionalDepartment(Long projectGroupId){
        if (projectGroupId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return projectService.reportToFunctionalDepartment(projectGroupId);
    }


    @ApiOperation(" 驳回项目立项申请（实验室，二级单位，职能部门通用接口）--可使用")
    @PostMapping(value = "/rejectProjectApply")
    public Result rejectProjectApply(@Valid @RequestBody List<ProjectCheckForm> formList){
        return projectService.rejectProjectApply(formList);
    }
}
