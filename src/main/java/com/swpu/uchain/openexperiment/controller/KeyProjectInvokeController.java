package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.certificate.DeleteCertificateForm;
import com.swpu.uchain.openexperiment.form.check.KeyProjectCheck;
import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.KeyProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author dengg
 */
@CrossOrigin
@RestController
@RequestMapping("/api/project")
@Api(tags = "项目（重点）模块执行接口")
public class KeyProjectInvokeController {

    private KeyProjectService keyProjectService;

    private GetUserService getUserService;


    @Autowired
    public KeyProjectInvokeController(KeyProjectService keyProjectService, GetUserService getUserService) {
        this.keyProjectService = keyProjectService;
        this.getUserService = getUserService;
    }

    @ApiOperation("添加标志性结果")
    @PostMapping("/insertIconicResult")
    public Result insertIconicResult(@Valid @RequestBody List<IconicResultForm> iconicResultForms){
        return keyProjectService.insertIconicResult(iconicResultForms);
    }

    @ApiOperation("删除一个标志性结果")
    @PostMapping("/deleteIconicResult")
    public Result deleteIconicResult(@Valid @RequestBody DeleteCertificateForm deleteCertificateForm){
        return keyProjectService.deleteIconicResult(deleteCertificateForm.getId());
    }

    @ApiOperation("重点项目申请接口")
    @PostMapping(value = "/createKeyApply")
    public Result createKeyApply(@Valid @RequestBody KeyProjectApplyForm form){
        return keyProjectService.createKeyApply(form);
    }

    @ApiOperation("删除重点项目，指导老师")
    @PostMapping("/deleteKeyProject")
    public Result deleteKeyProject(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.deleteKeyProject(list);
    }

    @ApiOperation("指导老师同意带审核项目")
    @PostMapping(value = "/agreeKeyProjectByGuideTeacher")
    public Result agreeKeyProjectByGuideTeacher(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.agreeKeyProjectByGuideTeacher(list);
    }


    @ApiOperation("实验室主任同意待审核项目")
    @PostMapping(value = "/agreeKeyProjectByLabAdministrator")
    public Result agreeKeyProjectByLabAdministrator(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.agreeKeyProjectByLabAdministrator(list);
    }

    @ApiOperation("二级单位主任同意待审核项目")
    @PostMapping(value = "/agreeKeyProjectBySecondaryUnit")
    public Result agreeKeyProjectBySecondaryUnit(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.agreeKeyProjectBySecondaryUnit(list);
    }

    @ApiOperation("职能部门同意立项")
    @PostMapping(value = "/agreeKeyProjectByFunctionalDepartment")
    public Result agreeKeyProjectByFunctionalDepartment(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.agreeKeyProjectByFunctionalDepartment(list);
    }

    @ApiOperation("职能部门同意中期检查项目")
    @PostMapping(value = "/agreeIntermediateInspectionKeyProject")
    public Result agreeIntermediateInspectionKeyProject(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.agreeIntermediateInspectionKeyProject(list);
    }

    @ApiOperation("职能部门同意结题")
    @PostMapping(value = "/agreeToBeConcludingKeyProject")
    public Result agreeToBeConcludingKeyProject(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.agreeToBeConcludingKeyProject(list);
    }

    @ApiOperation("实验室主任上报已审核项目")
    @PostMapping("/reportKeyProjectByLabAdministrator")
    public Result reportKeyProjectByLabAdministrator(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.reportKeyProjectByLabAdministrator(list);
    }

    @ApiOperation("重点项目评审打分")
    @PostMapping(value = "/collegeSetKeyScore")
    public Result collegeSetKeyScore(@Valid @RequestBody List<CollegeGiveScore> collegeGiveScores){
        return keyProjectService.collegeSetKeyScore(collegeGiveScores);
    }

    @ApiOperation("二级单位主任上报已审核项目")
    @PostMapping(value = "/reportKeyProjectBySecondaryUnit")
    public Result reportKeyProjectBySecondaryUnit(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.reportKeyProjectBySecondaryUnit(list);
    }

    @ApiOperation("指导老师拒绝带审核项目")
    @PostMapping(value = "/rejectKeyProjectByGuideTeacher")
    public Result rejectKeyProjectByGuideTeacher(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.rejectKeyProjectByGuideTeacher(list);
    }

    @ApiOperation("实验室主任拒绝待审核项目")
    @PostMapping(value = "/rejectKeyProjectByLabAdministrator")
    public Result rejectKeyProjectByLabAdministrator(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.rejectKeyProjectByLabAdministrator(list);
    }

    @ApiOperation("二级单位主任拒绝待审核项目")
    @PostMapping(value = "/rejectKeyProjectBySecondaryUnit")
    public Result rejectKeyProjectBySecondaryUnit(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.rejectKeyProjectBySecondaryUnit(list);
    }

    @ApiOperation("二级单位主任拒绝上报项目")
    @PostMapping(value = "/rejectKeyProjectReportBySecondaryUnit")
    public Result rejectKeyProjectReportBySecondaryUnit(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.rejectKeyProjectReportBySecondaryUnit(list);
    }

    @ApiOperation("职能部门拒绝待审核项目")
    @PostMapping(value = "/rejectKeyProjectByFunctionalDepartment")
    public Result rejectKeyProjectByFunctionalDepartment(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.rejectKeyProjectByFunctionalDepartment(list);
    }


    @ApiOperation("职能部门拒绝中期检查项目")
    @PostMapping(value = "/rejectIntermediateInspectionKeyProject")
    public Result rejectIntermediateInspectionKeyProject(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.rejectIntermediateInspectionKeyProject(list);
    }


    @ApiOperation("中期检查项目退回")
    @PostMapping("/KeyProjectMidTermKeyProjectHitBack")
    public Result midTermKeyProjectHitBack(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.midTermKeyProjectHitBack(list);
    }

    @ApiOperation("职能部门立项的时候退回")
    @PostMapping("/keyProjectEstablishHitBack")
    public Result keyProjectEstablishHitBack(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.keyProjectEstablishHitBack(list);
    }

    @ApiOperation("重点项目中期复核通过")
    @PostMapping("/KeyProjectMidTermReviewPassed")
    public Result midTermReviewPassed(@RequestBody List<KeyProjectCheck> list){
        return keyProjectService.midTermReviewPassed(list);
    }

    @ApiOperation("职能部门立项的复核通过")
    @PostMapping("/keyProjectEstablishReviewPassed")
    public Result keyProjectEstablishReviewPassed(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.keyProjectEstablishReviewPassed(list);
    }

    @ApiOperation("学院结题检查项目退回修改")
    @PostMapping("/collegeKeyProjectHitBack")
    public Result collegeKeyProjectHitBack(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.collegeKeyProjectHitBack(list);
    }

    @ApiOperation("重点项目学院复核通过")
    @PostMapping("/collegeReviewPassed")
    public Result collegeReviewPassed(@RequestBody List<KeyProjectCheck> list){
        return keyProjectService.collegeReviewPassed(list);
    }

    @ApiOperation("学院拒绝结题")
    @PostMapping(value = "/rejectCollegeKeyProject")
    public Result rejectCollegeKeyProject(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.rejectCollegeKeyProject(list);
    }

    @ApiOperation("学院初审检查通过------------------------------改了")
    @PostMapping("/collegeGivesKeyProjectRating")
    public Result collegeGivesKeyProjectRating(@RequestBody @Valid List<KeyProjectCheck> list){
        return keyProjectService.collegeGivesKeyProjectRating(list);
    }

    @ApiOperation("职能部门检查项目退回修改")
    @PostMapping("/functionKeyProjectHitBack")
    public Result functionKeyProjectHitBack(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.functionKeyProjectHitBack(list);
    }

    @ApiOperation("职能部门批量通过-----------改")
    @PostMapping("/functionGivesKeyProjectRating")
    public Result functionGivesKeyProjectRating(@RequestBody @Valid List<KeyProjectCheck> list){
        return keyProjectService.functionGivesKeyProjectRating(list);
    }

    @ApiOperation("重点项目职能部门复核通过")
    @PostMapping("/functionReviewPassedKeyProject")
    public Result functionReviewPassedKeyProject(@RequestBody List<KeyProjectCheck> list){
        return keyProjectService.functionReviewPassed(list);
    }

    @ApiOperation("职能部门拒绝结题")
    @PostMapping(value = "/rejectToBeConcludingKeyProject")
    public Result rejectToBeConcludingKeyProject(@Valid @RequestBody List<KeyProjectCheck> list){
        return keyProjectService.rejectToBeConcludingKeyProject(list);
    }
}
