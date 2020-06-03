package com.changgou.search.service;

import java.util.Map;

public interface SkuESService {
    /**
     * 分页将数据库数据导入ElasticSearch域中
     * @param page
     * @param size
     */
    void importData(Integer page,Integer size);

    /**
     * 根据用户传参查出对应的数据
     * @param map
     * @return
     */
    Map<String, Object> findSearchSkuMap(Map<String,String> map);
}
