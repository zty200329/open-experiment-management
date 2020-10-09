package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.domain.Acl;
import com.swpu.uchain.openexperiment.domain.Role;
import com.swpu.uchain.openexperiment.domain.RoleAcl;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.permission.AclUpdateForm;
import com.swpu.uchain.openexperiment.form.permission.RoleAclForm;
import com.swpu.uchain.openexperiment.form.permission.RoleForm;
import com.swpu.uchain.openexperiment.form.permission.UserRoleForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AclService;
import com.swpu.uchain.openexperiment.service.RoleAclService;
import com.swpu.uchain.openexperiment.service.RoleService;
import com.swpu.uchain.openexperiment.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.Context;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Map;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:  权限管理模块接口
 *
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/permission")
@Api(tags = "权限控制接口")
public class PermissionController implements InitializingBean {
    @Autowired
    private AclService aclService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleAclService roleAclService;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private RedisService redisService;

    @ApiIgnore
    @ApiOperation("获取所有Acl")
    @GetMapping(value = "/allAcl", name = "获取所有Acl")
    public Object allAcl(){
        return aclService.selectAll();
    }

    @ApiIgnore
    @ApiOperation("按关键字查找")
    @PostMapping(value = "/selectAclByRandom", name = "按关键字查找")
    public Object selectAclByRandom(String info){
        if (StringUtils.isEmpty(info)){
            return Result.success();
        }
        return aclService.selectByRandom(info);
    }

    @ApiOperation("根据id获取角色信息")
    @ApiIgnore
    @GetMapping(value = "/selectRole", name = "根据id获取角色信息")
    public Object selectRole(Long id){
        return Result.success(roleService.selectRoleInfo(id));
    }

    @ApiIgnore
    @ApiOperation("更新接口描述")
    @PostMapping(value = "/updateAclDescription", name = "更新接口描述")
    public Object updateAclDescription(@Valid @RequestBody AclUpdateForm aclUpdateForm){
        return aclService.updateAcl(aclUpdateForm);
    }

    @ApiIgnore
    @ApiOperation("添加角色的权限")
    @PostMapping(value = "/addRoleAcl",name = "添加角色的权限")
    public Object addRoleAcl(@Valid @RequestBody RoleAclForm roleAclForm){
        return roleAclService.addRoleAcl(roleAclForm);
    }

    @ApiOperation("获取拥有立项评审老师和结题评审老师的用户信息")
    @GetMapping("/getCollegeUserInfoByCollege")
    public Result getCollegeUserInfoByCollege(){
        return userRoleService.getCollegeUserInfoByCollege();
    }

    @ApiOperation("获取拥有实验室主任，学院领导，职能部门角色的用户信息")
    @GetMapping("/getUserInfoByRole")
    public Result getUserInfoByRole(){
        return userRoleService.getUserInfoByRole();
    }

    @ApiIgnore
    @ApiOperation("移除角色的权限")
    @PostMapping(value = "/deleteRoleAcl", name = "移除角色的权限")
    public Object deleteRoleAcl(Long roleId, Long aclId){
        if (roleId == 0 || aclId == 0){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        roleAclService.deleteByRoleIdAclId(roleId, aclId);
        return Result.success();
    }

    @ApiIgnore
    @ApiOperation("添加角色")
    @PostMapping(value = "/addRole", name = "添加角色")
    public Object addRole(String roleName){
        if (StringUtils.isEmpty(roleName)){
            return Result.error(CodeMsg.ROLE_NAME_CANT_BE_NULL);
        }
        return roleService.addRole(roleName);
    }

    @ApiIgnore
    @ApiOperation("删除角色")
    @PostMapping(value = "/deleteRole", name = "删除角色")
    public Object deleteRole(Long roleId){
        if (roleId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        roleService.delete(roleId);
        return Result.success();
    }

    @ApiIgnore
    @ApiOperation("更新角色名")
    @PostMapping(value = "/updateRoleName", name = "更新角色名")
    public Object updateRoleName(@Valid @RequestBody RoleForm roleForm){
        return roleService.updateRoleName(roleForm);
    }

    @ApiIgnore
    @ApiOperation("获取所有角色以及权限访问路径")
    @GetMapping(value = "/allRole", name = "获取所有角色")
    public Object allRole(){
        return roleService.selectAllRole();
    }

    @ApiOperation("添加用户的角色")
    @PostMapping(value = "/addUserRole", name = "添加用户的角色")
    public Object addUserRole(@Valid @RequestBody UserRoleForm userRoleForm){
        return userRoleService.addUserRole(userRoleForm);
    }

    @ApiOperation("移除用户的角色")
    @PostMapping(value = "/deleteUserRole", name = "移除用户的角色")
    public Result deleteUserRole(@Valid @RequestBody UserRoleForm userRoleForm){
        return userRoleService.deleteByUserIdRoleId(userRoleForm.getUserId(),userRoleForm.getRoleId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("================== flush()清空缓存 ==============");
        redisService.flush();
        RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) context.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        log.info("===============================更新数据库Acl数据=========================");
        Role admin = roleService.selectRoleName("ADMIN");
        if (admin == null){
            roleService.addRole("ADMIN");
        }
        for (RequestMappingInfo info : map.keySet()) {
            String url = info.getPatternsCondition().getPatterns().iterator().next();
            log.info("url:" + url);
            String name = info.getName();
            log.info("name:" + name);
            //跳过name为null的接口
            if (StringUtils.isEmpty(name)) {
                continue;
            }
            Acl acl = aclService.selectByUrl(url);
            //当数据库存在当前接口则进行检查是否为管理员添加过权限
            if (acl != null){
                RoleAcl roleAcl = roleAclService.selectByRoleIdAndAclId(admin.getId(), acl.getId());
                if (roleAcl == null){
                    log.info("添加ADMIN权限： {}", acl);
                    roleAclService.addRoleAcl(new RoleAclForm(admin.getId(), acl.getId()));
                }
                continue;
            }
            acl = new Acl();
            acl.setUrl(url);
            acl.setName(name);
            if (aclService.insert(acl)) {
                log.info("添加acl: {}", acl);
                roleAclService.addRoleAcl(new RoleAclForm(admin.getId(), acl.getId()));
                log.info("同时添加ADMIN权限： {}", acl);
            }
        }
        log.info("=============================更新Acl完成================================");

    }
}
