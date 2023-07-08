package com.qpf.basic.controller;

import com.qpf.basic.service.IFastDfsService;
import com.qpf.basic.service.impl.FastDfsServiceImpl;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 上传
 * 下载 = 不需要
 * 查看 = 路径 = http://ip:8888/组名/......
 * 删除
 * 更新 = 删除 + 上传
 */
@RestController
@RequestMapping("/fastDfs")
public class FastDfsController {

    @Autowired
    private IFastDfsService fastDfsService;

    @PostMapping
    public AjaxResult uploadFile(@RequestPart("file") MultipartFile file) {
        try {
            String filePath = fastDfsService.uploadFile(file);
            return AjaxResult.me().setResultObj(filePath); //把上传后的路径返回回去
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("上传失败!" + e.getMessage());
        }
    }

    /**
     * 参数：必须是完整路径/group1/..., 或者带http的完整路径. http://服务地址/...
     * 返回值：成功与否
     */
    @DeleteMapping
    public AjaxResult deleteFile(@RequestParam(required = true, value = "path") String path) {
        try {
            fastDfsService.deleteFile(path);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败!" + e.getMessage());
        }
    }
}