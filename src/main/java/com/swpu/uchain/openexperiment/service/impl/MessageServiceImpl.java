package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.ProjectHistoryInfo;
import com.swpu.uchain.openexperiment.VO.MessageVO;
import com.swpu.uchain.openexperiment.dao.OperationRecordMapper;
import com.swpu.uchain.openexperiment.dao.UserProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.MemberRole;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.MessageService;
import com.swpu.uchain.openexperiment.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author panghu
 */
@Service
public class MessageServiceImpl implements MessageService {

    private GetUserService getUserService;

    private OperationRecordMapper operationRecordMapper;

    private UserProjectGroupMapper userProjectGroupMapper;

    @Autowired
    public MessageServiceImpl(GetUserService getUserService, OperationRecordMapper operationRecordMapper,
                              UserProjectGroupMapper userProjectGroupMapper) {
        this.getUserService = getUserService;
        this.operationRecordMapper = operationRecordMapper;
        this.userProjectGroupMapper = userProjectGroupMapper;
    }

    @Override
    public Result getAllMassage() {
        User user = getUserService.getCurrentUser();
        Long userId = Long.valueOf(user.getCode());
        List<Long> projectGroupIdList = userProjectGroupMapper.selectProjectGroupIdByUserIdAndMemberRole(userId, MemberRole.GUIDANCE_TEACHER.getValue());
        List<ProjectHistoryInfo> list = operationRecordMapper.selectAllByProjectIdList(projectGroupIdList);
        return Result.success(list);
    }

    @Override
    public Result readMessage(Long id) {
        return null;
    }
}
