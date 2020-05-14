package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author panghu
 */
@CrossOrigin
@RestController
@RequestMapping("/api/info")
@Api(tags = "消息模块接口")
public class MessageController {

    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @ApiOperation("获取自己的消息--可使用")
    @GetMapping("/getAllMassage")
    public Result getAllMassage(){
        return messageService.getAllMassage();
    }

    @ApiOperation("阅读消息--可使用")
    @GetMapping("/readMessage")
    public Result readMessage(Long id){
        return messageService.readMessage(id);
    }


    @ApiOperation("用户获取自己的消息提示")
    @GetMapping("/getMessageTips")
    public Result getMessageTips(){
        return messageService.getMessageTips();
    }

    @ApiOperation("用户")
    @GetMapping("/getAllMyMessage")
    public Result getAllMyMessage(){
        return null;
    }

}
