package cn.itchen.controller;

import cn.itchen.entity.FileInfo;
import cn.itchen.entity.User;
import cn.itchen.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/file")
public class FilesController {
    @Value("${file.dir}")
    private String filePath;
    @Autowired
    private FileService service;
    @RequestMapping("toLogin")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/showAll")
    public String showAll(HttpServletRequest request,Model model){

        User user = (User) request.getSession().getAttribute("loginUser");
        if(user!=null){
            List<FileInfo> files = service.findByUid(user.getId());
            model.addAttribute("files",files);
            return "myFile";
        }
        model.addAttribute("noLogin","你还未登录，请先<a href='toLogin'>登录</a>");
        return "myFile";
    }
    @PostMapping("upload")
    public String upload(MultipartFile file, HttpSession session) throws IOException {
        //获取当前登录用户
        User user=null;
        //先判断是否登录，没登录先登录否则强转
        if(session.getAttribute("loginUser")==null){
            session.setAttribute("notLogin","请您先登录");
            return "redirect:showAll";
        }
        System.out.println("1111111111111111"+file.getName());
        if (file.getSize()==0){
            session.setAttribute("error","请您选择文件");
            return "redirect:showAll";
        }
        //获取文件大小
        long size = file.getSize();
        //获取user对象
        user=(User) session.getAttribute("loginUser");
        //获取他的id
        Integer userId=user.getId();

        //获取文件属性
        String type = file.getContentType();
        //设置下载次数为0，刚创建
        System.out.println(filePath);
//        String realpath=ResourceUtils.getURL("classpath:").getPath()+ "static/files";
        String dateDir = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //获取上传路径
        String realPath = filePath + "/files";
        File path = new File(realPath, dateDir);
        if(!path.exists())path.mkdirs();
        String uuid=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+UUID.randomUUID().toString().replaceAll("-","");
        //后缀名
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());

        //老文件名
        String oldFileName = file.getOriginalFilename();
        //新文件名
        String newFileName=uuid+"."+ext;
        file.transferTo(new File(path,newFileName));
        FileInfo fileInfo = new FileInfo();
        fileInfo.setOldFileName(oldFileName).setNewFileName(newFileName).setUserId(userId).setExt(ext)
                .setSize((int)size).setType(type).setDownCounts(0).setUploadTime(new Date())
                .setPath("files/"+dateDir);
        System.out.println(fileInfo);
        service.save(fileInfo);
        return "redirect:showAll";
    }
    @RequestMapping("/downLoad")
    public void downLoad(@RequestParam(name = "isDownLoad",required = true,defaultValue = "n") String isDownLoad, Integer id, HttpServletResponse response) throws IOException {
        boolean download=isDownLoad.equalsIgnoreCase("y")?true:false;
        //查询出这个文件

        FileInfo fileInfo=service.findById(id);
        //找到文件在服务器的绝对路径
        String realPath = filePath+fileInfo.getPath();
        //获取文件名称
        String fileName = fileInfo.getNewFileName();
        //根据路径和名称拿到文件
        System.out.println(realPath+"/"+fileName);
        File file = new File(realPath, fileName);
        //判断是否为下载，是的话设置响应头，增加文件下载次数
        if (download){response.setHeader("content-disposition","attachment;fileName="+fileInfo.getOldFileName());service.addDownCount(id);}
        //将文件发送给用户
        FileInputStream is = new FileInputStream(file);
        ServletOutputStream os = response.getOutputStream();
        IOUtils.copy(is,os);
        //关闭流
        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(is);


    }
//    @RequestMapping("/show")
//    public void show(Integer id,HttpServletResponse response) throws IOException {
//        //查询出这个文件
//        FileInfo fileInfo=service.findById(id);
//        //找到文件在服务器的绝对路径
//        String realPath = ResourceUtils.getURL("classpath:").getPath() + "static/" + fileInfo.getPath();
//        //获取文件名称
//        String fileName = fileInfo.getNewFileName();
//        //根据路径和名称拿到文件
//        File file = new File(realPath, fileName);
//        //设置响应头为附件形式
//        //将文件发送给用户
//        FileInputStream is = new FileInputStream(file);
//        ServletOutputStream os = response.getOutputStream();
//        IOUtils.copy(is,os);
//        //关闭流
//        IOUtils.closeQuietly(os);
//        IOUtils.closeQuietly(is);
//    }
    @RequestMapping("/delete")
    public String delete(Integer id,Model model) throws FileNotFoundException {
        FileInfo fileInfo = service.findById(id);
        if(fileInfo!=null){
//            String realPath = ResourceUtils.getURL("classpath:").getPath() + "static/" + fileInfo.getPath();
            File file = new File(filePath+fileInfo.getPath(), fileInfo.getNewFileName());
            if (file.exists())file.delete();
            service.delete(id);
            model.addAttribute("deletePass","删除成功");
        }
        return "forward:showAll";
    }
    @RequestMapping("/findAllJSON")
    @ResponseBody
    public List<FileInfo> findAllJSON(HttpServletRequest request,Model model){

        User user = (User) request.getSession().getAttribute("loginUser");
        if(user!=null){
            List<FileInfo> files = service.findByUid(user.getId());

            return files;
        }
        model.addAttribute("noLogin","你还未登录，请先<a href='toLogin'>登录</a>");
        return null;
    }
}
