package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.ProjectStatus;
import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import com.swpu.uchain.openexperiment.util.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-21
 * @Description:
 * 项目控制接口
 */
@CrossOrigin
@RestController
@RequestMapping("/project")
@Api(tags = "项目模块接口")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserProjectService userProjectService;



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


    @ApiOperation("申请参与项目接口--可使用")
    @PostMapping(value = "/joinApply", name = "申请参与项目接口")
    public Result joinApply(@Valid JoinProjectApplyForm joinProjectApplyForm){
        return userProjectService.applyJoinProject(joinProjectApplyForm);
    }

    @ApiOperation("获取当前用户参与的某状态的项目信息, 项目状态: -1(所有), 0(申报), 1(立项), 2(驳回修改),3(已上报学院领导), 4(中期检查), 5(结项)")
    @GetMapping(value = "/getOwnProjects", name = "获取自己相关的项目信息")
    public Object getOwnProjects(int projectStatus){
        return projectService.getCurrentUserProjects(projectStatus);
    }

    @ApiOperation("获取项目的立项信息--可使用")
    @PostMapping(value = "/getApplyInfo", name = "获取项目的立项信息")
    public Result getApplyInfo(Long projectGroupId){
        return projectService.getApplyForm(projectGroupId);
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

    @ApiOperation("实验室获取待审核的项目（1）--可使用")
    @GetMapping(value = "/getDeclareProjectList", name = "审批项目展示接口")
    public Result getDeclareProjectList (Integer pageNum){
        if (pageNum == null || pageNum <= 0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        return projectService.getCheckInfo(pageNum, ProjectStatus.DECLARE.getValue());
    }

    @ApiOperation("二级单位获取待审核的项目（2）--可使用")
    @GetMapping(value = "/getLabAllowedProjectList", name = "审批项目展示接口")
    public Result getLabAllowedProjectList (Integer pageNum){
        if (pageNum == null || pageNum <= 0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        return projectService.getCheckInfo(pageNum, ProjectStatus.LAB_ALLOWED.getValue());
    }

    @ApiOperation("职能部门获取待立项审核的项目（3）--可使用")
    @GetMapping(value = "/getSecondaryUnitAllowedProjectList", name = "审批项目展示接口")
    public Result getSecondaryUnitAllowedProjectList (Integer pageNum){
        if (pageNum == null || pageNum <= 0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        return projectService.getCheckInfo(pageNum, ProjectStatus.SECONDARY_UNIT_ALLOWED.getValue());
    }




    @ApiOperation("同意立项--待完成")
    @PostMapping(value = "/agreeEstablish", name = "同意立项")
    public Object agreeEstablish(List<Long> projectGroupIdList){
        return projectService.agreeEstablish(projectGroupIdList);
    }

    @ApiOperation("驳回修改--待完成")
    @PostMapping(value = "/rejectModifyApply", name = "驳回修改")
    public Object rejectModifyApply(Long projectGroupId){
        if (projectGroupId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return projectService.rejectModifyApply(projectGroupId);
    }

    @ApiOperation("修改项目组成员身份")
    @PostMapping(value = "/aimMemberLeader", name = "修改项目组成员身份")
    public Object aimMemberLeader(@Valid @RequestBody AimForm aimForm){
        return userProjectService.aimUserMemberRole(aimForm);
    }


    @ApiOperation("追加立项申请内容(资金申请报账)--可使用")
    @PostMapping(value = "/appendCreateApply", name = "追加立项申请内容")
    public Object appendCreateApply(@Valid @RequestBody AppendApplyForm appendApplyForm){
        return projectService.appendCreateApply(appendApplyForm);
    }

    @GetMapping(value = "/getApplyingJoinInfo", name = "获取当前用户（限老师身份）指导项目的申请参加列表")
    @ApiOperation("获取当前用户（限老师身份）指导项目的申请参加列表--可使用")
    public Object getApplyingJoinInfo(){
        return Result.success(projectService.getJoinInfo());
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


    @ApiOperation("上报学院领导--待完成")
    @PostMapping(value = "/reportToCollegeLeader", name = "上报学院领导")
    public Object reportToCollegeLeader(Long projectGroupId){
        if (projectGroupId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return projectService.reportToCollegeLeader(projectGroupId);
    }

    @ApiOperation("根据项目名模糊查询项目")
    @GetMapping(value = "/selectProject", name = "根据项目名模糊查询项目")
    public Object selectProject(String name){
        if (StringUtils.isEmpty(name)){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return Result.success(projectService.selectByProjectName(name));
    }
}
