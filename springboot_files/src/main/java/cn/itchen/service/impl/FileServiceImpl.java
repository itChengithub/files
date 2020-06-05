package cn.itchen.service.impl;

import cn.itchen.dao.FileDao;
import cn.itchen.entity.FileInfo;
import cn.itchen.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class FileServiceImpl implements FileService {
    @Autowired
    private FileDao dao;
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<FileInfo> findByUid(Integer uid) {
        return dao.findByUid(uid);
    }

    @Override
    public void save(FileInfo fileInfo) {
        //判断是否是图片
        String type = fileInfo.getType();
        if(type.startsWith("image")){
            fileInfo.setIsImg("是");
        }else{
            fileInfo.setIsImg("否");
        }
        dao.save(fileInfo);
    }

    @Override
    public void delete(Integer id) {
        dao.delete(id);
    }

    @Override
    public void addDownCount(Integer id) {
        dao.addDownCount(id);
    }

    @Override
    public FileInfo findById(Integer id) {
        return dao.findById(id);
    }
}
