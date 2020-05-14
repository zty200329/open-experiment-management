package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.ProjectHistoryInfo;
import com.swpu.uchain.openexperiment.domain.HitBackMessage;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.mapper.HitBackMessageMapper;
import com.swpu.uchain.openexperiment.mapper.OperationRecordMapper;
import com.swpu.uchain.openexperiment.mapper.UserProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.MemberRole;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.MessageService;
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

    private HitBackMessageMapper hitBackMessageMapper;

    @Autowired
    public MessageServiceImpl(GetUserService getUserService, OperationRecordMapper operationRecordMapper,
                              UserProjectGroupMapper userProjectGroupMapper,HitBackMessageMapper hitBackMessageMapper) {
        this.getUserService = getUserService;
        this.operationRecordMapper = operationRecordMapper;
        this.userProjectGroupMapper = userProjectGroupMapper;
        this.hitBackMessageMapper = hitBackMessageMapper;
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

    /**
     * 查看是否有消息
     * @return
     */
    @Override
    public Result getMessageTips() {
        User currentUser = getUserService.getCurrentUser();
        if (currentUser == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        List<HitBackMessage> hitBackMessages = hitBackMessageMapper.selectByUserIdAndNotRead(Long.valueOf(currentUser.getCode()));
        return Result.success(hitBackMessages);
    }

    @Override
    public Result getAllMyMessage() {
        User currentUser = getUserService.getCurrentUser();
        if (currentUser == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        List<HitBackMessage> hitBackMessages = hitBackMessageMapper.selectByUserId(Long.valueOf(currentUser.getCode()));
        return Result.success(hitBackMessages);
    }

    @Override
    public Result confirmReceiptOfMidtermReminder(Long id) {
        User currentUser = getUserService.getCurrentUser();
        if (currentUser == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        HitBackMessage hitBackMessage = hitBackMessageMapper.selectByPrimaryKey(id);
        hitBackMessage.setIsRead(true);
        hitBackMessageMapper.updateByPrimaryKey(hitBackMessage);
        return Result.success();
    }
}
