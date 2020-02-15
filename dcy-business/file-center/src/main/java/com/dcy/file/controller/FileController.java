package com.dcy.file.controller;

import com.dcy.common.model.ResponseData;
import com.dcy.db.base.model.PageHelper;
import com.dcy.file.model.FileInfo;
import com.dcy.file.service.IFileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2019/9/18 14:02
 */
@RestController
@RequestMapping("/file")
@Api(value = "FileController", tags = {"文件操作接口"})
public class FileController {

    @Autowired
    private IFileInfoService iFileInfoService;

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageHelper", value = "分页信息", dataType = "PageHelper", paramType = "query"),
            @ApiImplicitParam(name = "fileInfo", value = "查询对象", dataType = "FileInfo", paramType = "query")
    })
    @GetMapping(value = "/page")
    public ResponseData page(PageHelper<FileInfo> pageHelper, FileInfo fileInfo) {
        return ResponseData.success(iFileInfoService.pageList(pageHelper, fileInfo));
    }


    /**
     * 文件上传
     * 根据fileType选择上传方式
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public ResponseData upload(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseData.success(iFileInfoService.upload(file));
    }

    /**
     * 文件删除
     *
     * @param id
     */
    @ApiOperation(value = "文件删除", notes = "文件删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id", dataType = "String", paramType = "query")
    })
    @PostMapping("/delete")
    public ResponseData delete(String id) {
        iFileInfoService.deleteFile(id);
        return ResponseData.success();
    }


}
