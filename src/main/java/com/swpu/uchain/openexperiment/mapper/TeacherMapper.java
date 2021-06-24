package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.User;
import org.springframework.stereotype.Repository;

/**
 * @author: panghu
 * @Description:
 * @Date: Created in 20:17 2020/2/4
 * @Modified By:
 */
@Repository
public interface TeacherMapper {

    User selectByUserCode(String userCode);

}
