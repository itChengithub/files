package cn.itchen.service;

import cn.itchen.entity.FileInfo;

import java.util.List;

public interface FileService {
    List<FileInfo> findByUid(Integer uid);

    void save(FileInfo fileInfo);

    FileInfo findById(Integer id);

    void addDownCount(Integer id);

    void delete(Integer id);
}
