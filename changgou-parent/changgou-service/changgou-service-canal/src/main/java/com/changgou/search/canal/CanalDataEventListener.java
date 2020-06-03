package com.changgou.search.canal;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.search.content.pojo.Content;
import com.changgou.search.feign.ContentFeign;
import com.changgou.goods.entity.Result;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CanalEventListener//开启canal配置
public class CanalDataEventListener {
    @Autowired
    private ContentFeign contentFeign;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /**
     * 添加监听
     * rowData.getBeforeColumnsList():操作之前的数据  用于：增、改操作
     * rowData.getAfterColumnsList():操作之后的数据   用于：删、改操作
     * @param eventType  当前操作的属性
     * @param rowData    操作的一行数据
     */
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : afterColumnsList) {
            System.out.println("列名："+column.getName()+"------------------操作之后的数据名"+column.getValue());
        }

    }
    /**
     * 修改监听
     * rowData.getBeforeColumnsList():操作之前的数据  用于：增、改操作
     * rowData.getAfterColumnsList():操作之后的数据   用于：删、改操作
     * @param eventType  当前操作的属性
     * @param rowData    操作的一行数据
     */
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();

        for (CanalEntry.Column column : beforeColumnsList) {
            System.out.println("列名："+column.getName()+"------------------修改之前的数据名"+column.getValue());
        }
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : afterColumnsList) {
            System.out.println("列名："+column.getName()+"------------------修改之后的数据名"+column.getValue());
        }

    }
    /**
     * 删除监听
     * rowData.getBeforeColumnsList():操作之前的数据  用于：增、改操作
     * rowData.getAfterColumnsList():操作之后的数据   用于：删、改操作
     * @param eventType  当前操作的属性
     * @param rowData    操作的一行数据
     */
    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        for (CanalEntry.Column column : beforeColumnsList) {
            System.out.println("列名："+column.getName()+"------------------删除之前的数据名"+column.getValue());
        }

    }
    /**
     * 自定义监听
     * rowData.getBeforeColumnsList():操作之前的数据  用于：增、改操作
     * rowData.getAfterColumnsList():操作之后的数据   用于：删、改操作
     * @param eventType  当前操作的属性
     * @param rowData    操作的一行数据
     */
    @ListenPoint(
            eventType = {CanalEntry.EventType.DELETE,CanalEntry.EventType.UPDATE,CanalEntry.EventType.INSERT},//监听哪些操作
            schema = {"changgou_content"},      //指定监听的数据库
            table = {"tb_content"},//指定监听哪张表
            destination = "example"//指定实例的地址
            )
    public void onEventCustomUpdate(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        String category_idStr=null;
        Long category_id=null;
        Result<List<Content>> result=null;
        List<Content> contents=null;
        String contentsStr=null;
        Map<String, String> map =null;
        if(afterColumnsList!=null){
            //获取修改数据之后的数据的map集合
            map = columnListToMap(afterColumnsList);
            //拿出category_id的属性值
            category_idStr = map.get("category_id");
            //将他转为Long类型的整数
            category_id = Long.parseLong(category_idStr);
            //用它当作参数，通过contentFeign查询数据库对应的这个分类id的返回值结果信息，包含了广告集合信息
            result = contentFeign.findByCategoryId(category_id);
            //拿出广告集合信息
            contents = result.getData();
            //将广告集合转换为json字符串格式
            contentsStr = JSON.toJSONString(contents);
            //将content_+id设为key表示那个类型的广告redis数据，value值为广告集合的json字符串
            redisTemplate.opsForValue().set("content_"+category_id,contentsStr);
        }

        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        if(beforeColumnsList!=null){
            map = columnListToMap(beforeColumnsList);

            category_idStr = map.get("category_id");
            category_id = Long.parseLong(category_idStr);
            result = contentFeign.findByCategoryId(category_id);
            System.out.println("------------------22222222--------------------------"+category_id);
            contents= result.getData();
            contentsStr = JSON.toJSONString(contents);

            System.out.println(contentsStr);
            redisTemplate.opsForValue().set("content_"+category_id,contentsStr);
        }
    }
    public Map<String,String> columnListToMap(List<CanalEntry.Column> list){
        Map<String,String> map=new HashMap<>();
        for (CanalEntry.Column column : list) {
            map.put(column.getName(),column.getValue());
        }
        return map;
    }
}
