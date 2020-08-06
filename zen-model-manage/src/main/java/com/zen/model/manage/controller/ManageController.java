package com.zen.model.manage.controller;

import com.zen.model.manage.dto.ModelInfoDto;
import com.zen.model.manage.service.ManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.ValidationException;

/**
 * @Author: morris
 * @Date: 2020/6/11 15:41
 * @description
 * @reviewer
 */
@Api("模型管理")
@RestController
@RequestMapping("/ad/model")
public class ManageController {

    @Autowired
    private ManageService manageService;

    @ApiOperation(value = "创建模型")
    @PostMapping("/createModel")
    public Object createModel(@RequestBody(required = true) ModelInfoDto modelInfoDto){
        return manageService.createModel(modelInfoDto);
    }


    @PostMapping("/updateModel")
    @ApiOperation("更新模型")
    public Object updateModel(@RequestBody(required = true) ModelInfoDto modelInfoDto) throws ValidationException {
        return manageService.updateModel(modelInfoDto);
    }

    @GetMapping("/offlineModel")
    @ApiOperation("下线模型")
    public Object offlineModel(@RequestParam(required = true,name = "token") String token) throws ValidationException {
        return manageService.offlineModel(token);
    }

    @GetMapping("/rollbackModel")
    @ApiOperation("模型版本回滚")
    public Object rollbackModel(@RequestParam(required = true,name = "token") String token) throws ValidationException {
        return manageService.rollbackModel(token);
    }


    @GetMapping("/deleteModel")
    @ApiOperation("删除模型")
    public Object deleteModel(@RequestParam(required = true,name = "token") String token,
                              @RequestParam(required = true,name = "version") Integer version) throws ValidationException {

        return manageService.deleteModel(token,version);
    }

    @PostMapping("/uploadModelAndUpdate")
    @ApiOperation("上传模型并更新模型")
    public Object uploadModelAndUpdate(@RequestParam("file") MultipartFile file,@RequestParam(value = "path",required = false)  String path,@RequestParam("token")String token) throws Exception {

        System.out.println(file.getOriginalFilename());
        return manageService.uploadModelFile(path,file,token);
    }

    @PostMapping("/uploadModel")
    @ApiOperation("上传模型")
    public Object uploadModel(@RequestParam("file") MultipartFile file,@RequestParam(value = "path",required = false)  String path) throws Exception {

        System.out.println(file.getOriginalFilename());
        return manageService.uploadModelFile(path,file);
    }

    @GetMapping("/getModelList")
    @ApiOperation("获取所有模型list")
    public Object getModelList(@RequestParam(value = "status",required = false) Boolean online){
        return manageService.getModelList(online);
    }


}
