package cn.itchen.dao;

import cn.itchen.entity.FileInfo;

import java.util.List;

public interface FileDao {
    List<FileInfo> findByUid(Integer uid);

    void save(FileInfo fileInfo);

    FileInfo findById(Integer id);

    void addDownCount(Integer id);

    void delete(Integer id);
}
