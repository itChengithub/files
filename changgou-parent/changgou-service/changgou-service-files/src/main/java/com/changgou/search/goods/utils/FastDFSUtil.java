package com.changgou.search.goods.utils;

import com.changgou.search.goods.file.FastDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

public class FastDFSUtil {
    static {
        //找到我们的配置类，类中写了参数及其Tracker的路径
        String fileName=new ClassPathResource("fdfs_client.conf").getPath();
        try {
            //使用ClientGlobal初始化记载链接信息
            ClientGlobal.init(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String[] upload(FastDFSFile fastDFSFile) throws Exception{
        System.out.println("拿到了文件"+fastDFSFile.getName());
        NameValuePair[] nameValues=new NameValuePair[1];
        nameValues[0]=new NameValuePair("作者",fastDFSFile.getAuthor());
        //获取Tracker客户端对象
        StorageClient storageClient = getStorageClient();
        return storageClient.upload_file(fastDFSFile.getContent(),fastDFSFile.getExt(),null);


    }
    public static InputStream down(String groupName, String remoteFileName) throws Exception{
        //获取Tracker客户端对象
        StorageClient storageClient = getStorageClient();
        byte[] bytes = storageClient.download_file(groupName, remoteFileName);
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        return is;
    }
    public static Integer delete(String groupName, String remoteFileName) throws Exception{

        StorageClient storageClient = getStorageClient();
        return getStorageClient().delete_file(groupName, remoteFileName);
    }
    public static StorageClient getStorageClient() throws IOException {
        //获取Tracker客户端对象
        TrackerClient trackerClient = new TrackerClient();
        //通过客户端对象访问Server端，获取可用的Storage对象
        TrackerServer connection =trackerClient.getConnection();
        //创建Storage对象，上传文件
        return new StorageClient(connection,null);
    }
    //获取Storage信息
    public static StorageServer getStorage() throws IOException {
        //获取Tracker客户端对象
        TrackerClient trackerClient = new TrackerClient();
        //通过客户端对象访问Server端，获取可用的Storage对象
        TrackerServer connection =trackerClient.getConnection();
        //创建Storage对象，上传文件
        return trackerClient.getStoreStorage(connection);
    }
    //获取Storage的ip及其端口号
    public static ServerInfo[] getStorageInfo(String groupName, String remoteFileName) throws IOException {
        //获取Tracker客户端对象
        TrackerClient trackerClient = new TrackerClient();
        //通过客户端对象访问Server端，获取可用的Storage对象
        TrackerServer connection =trackerClient.getConnection();
        return trackerClient.getFetchStorages(connection,groupName,remoteFileName);
    }
    //获取Tracker的url
    public static String getTracker() throws IOException {
        //获取Tracker客户端对象
        TrackerClient trackerClient = new TrackerClient();
        //通过客户端对象访问Server端，获取可用的Storage对象
        TrackerServer connection =trackerClient.getConnection();
        int trackerPort = ClientGlobal.getG_tracker_http_port();
        InetSocketAddress inetSocketAddress = connection.getInetSocketAddress();
        return "http://"+inetSocketAddress+":"+trackerPort;
    }
    //获取Tracker的url

}
