package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.domain.Acl;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.permission.AclUpdateForm;
import com.swpu.uchain.openexperiment.form.permission.RoleAclForm;
import com.swpu.uchain.openexperiment.form.permission.RoleForm;
import com.swpu.uchain.openexperiment.form.permission.UserRoleForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AclService;
import com.swpu.uchain.openexperiment.service.RoleAclService;
import com.swpu.uchain.openexperiment.service.RoleService;
import com.swpu.uchain.openexperiment.service.UserRoleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.validation.Valid;
import java.util.Map;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 * 权限管理模块接口
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/permission")
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

    @GetMapping(value = "/allAcl", name = "获取所有Acl")
    public Object allAcl(){
        return aclService.selectAll();
    }

    @PostMapping(value = "/selectAclByRandom", name = "按关键字查找")
    public Object selectAclByRandom(String info){
        if (StringUtils.isEmpty(info)){
            return Result.success();
        }
        return aclService.selectByRandom(info);
    }

    @PostMapping(value = "/updateAclDescription", name = "更新接口描述")
    public Object updateAclDescription(@Valid AclUpdateForm aclUpdateForm){
        return aclService.updateAcl(aclUpdateForm);
    }

    @PostMapping(value = "/addRoleAcl",name = "批量添加角色的权限")
    public Object addRoleAcl(@Valid RoleAclForm roleAclForm){
        return roleAclService.addRoleAcl(roleAclForm);
    }

    @PostMapping(value = "/deleteRoleAcl", name = "移除角色的权限")
    public Object deleteRoleAcl(Long id){
        roleAclService.delete(id);
        return Result.success();
    }

    @PostMapping(value = "/addRole", name = "添加角色")
    public Object addRole(String roleName){
        if (StringUtils.isEmpty(roleName)){
            return Result.error(CodeMsg.ROLE_NAME_CANT_BE_NULL);
        }
        return roleService.addRole(roleName);
    }

    @PostMapping(value = "/deleteRole", name = "删除角色")
    public Object deleteRole(Long roleId){
        if (roleId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        roleService.delete(roleId);
        return Result.success();
    }

    @PostMapping(value = "/updateRoleName", name = "更新角色名")
    public Object updateRoleName(@Valid RoleForm roleForm){
        return roleService.updateRoleName(roleForm);
    }

    @GetMapping(value = "/allRole", name = "获取所有角色")
    public Object allRole(){
        return roleService.selectAllRole();
    }

    @PostMapping(value = "/addUserRole", name = "添加用户的角色")
    public Object addUserRole(@Valid UserRoleForm userRoleForm){
        return userRoleService.addUserRole(userRoleForm);
    }

    @PostMapping(value = "/deleteUserRole", name = "移除用户的角色")
    public Object deleteUserRole(Long userRoleId){
        if (userRoleId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        userRoleService.delete(userRoleId);
        return Result.success();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) context.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        log.info("===============================更新数据库Acl数据=========================");
        for (RequestMappingInfo info : map.keySet()) {
            String url = info.getPatternsCondition().getPatterns().iterator().next();
            log.info("url:" + url);
            String name = info.getName();
            log.info("name:" + name);
            Acl acl = aclService.selectByUrl(url);
            //当数据库存在或name为空(非自定义的接口)就直接跳过添加操作
            if (acl != null || StringUtils.isEmpty(name)){
                continue;
            }
            acl = new Acl();
            acl.setUrl(url);
            acl.setName(name);
            aclService.insert(acl);
            log.info("添加: {}", acl);
        }
        log.info("=============================更新Acl完成================================");
    }
}
