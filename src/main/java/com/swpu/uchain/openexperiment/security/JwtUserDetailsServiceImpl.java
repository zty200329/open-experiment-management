package com.swpu.uchain.openexperiment.security;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.service.AclService;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 实现UserDetailsService
 */
@Service
@Slf4j
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AclService aclService;
    @Autowired
    private GetUserService getUserService;

    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        //从数据库查询username,如果不存在则抛出异常
        User user = getUserService.selectByUserCode(userCode);
        if (user==null) {
            log.info("认证邮箱信息不存在");
            throw new UsernameNotFoundException(String.format(" user not exist with stuId ='%s'.", userCode));
        } else {
            //若存在则返回userDetails对象
            List<String> aclUrl = aclService.getUserAclUrl(user.getId());
            return new JwtUser(userCode, passwordEncoder.encode(user.getPassword()), aclUrl);
        }
    }
}
