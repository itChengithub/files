package com.changgou.goods.controller;

import com.changgou.goods.entity.Result;
import com.changgou.goods.entity.StatusCode;
import com.changgou.search.goods.file.FastDFSFile;
import com.changgou.search.goods.utils.FastDFSUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @PostMapping
    public Result upload(@RequestParam("file")MultipartFile file) throws Exception {
        FastDFSFile fastDFSFile=new FastDFSFile(
                file.getOriginalFilename(),
                file.getBytes(),
                StringUtils.getFilenameExtension(file.getOriginalFilename())
        );

        String[] fileMsg = FastDFSUtil.upload(fastDFSFile);
        String url=FastDFSUtil.getTracker()+fileMsg[0]+"/"+fileMsg[1];

        return new Result(true, StatusCode.OK,"上传成功",url);
    }
    @PostMapping
    public Result delete(String groupName,String remoteFileName) throws Exception {
        FastDFSUtil.delete(groupName,remoteFileName);


        return new Result(true, StatusCode.OK,"删除成功");
    }
}
