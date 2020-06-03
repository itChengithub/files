package com.changgou.seckill.timer;

import com.changgou.goods.util.DateUtil;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

@Component
public class SeckillGoodsPushTask {
    private int i=0;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Scheduled(cron = "0/5 * * * * ?")
    public void queryGoods() throws ParseException {
        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date dateMenu : dateMenus) {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHH");
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            String nameSpace = "Seckill_"+simpleDateFormat.format(dateMenu);
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status","1");
            criteria.andGreaterThan("stockCount",0);
            String s="2020012814";
            String s2="2020012816";
            Date endTime = simpleDateFormat.parse(s2);

            Date startTime = simpleDateFormat.parse(s);
            criteria.andGreaterThanOrEqualTo("startTime",startTime);
            System.out.println(dateMenu);
            Set keys = redisTemplate.boundHashOps(nameSpace).keys();
            if(keys!=null&&keys.size()!=0){
                criteria.andNotIn("id",keys);
            }
            criteria.andLessThan("endTime",endTime);
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);
            System.out.println(seckillGoods.size());
            for (SeckillGoods seckillGood : seckillGoods) {
                Object o = redisTemplate.boundHashOps(nameSpace).get(seckillGood.getId());
                if(o!=null){
                    continue;
                }
                redisTemplate.boundHashOps(nameSpace).put(seckillGood.getId(),seckillGood);
                System.out.println("商品ID："+seckillGood.getId()+"存到了Redis");
                redisTemplate.boundListOps("SeckillGoodsCountList_"+seckillGood.getId()).leftPushAll(findByCountArray(seckillGood.getStockCount(),seckillGood.getId()));
            }
            System.out.println("第"+i+"次输出");
        }
        i++;
    }

    /**
     * 根据订单的id和库存返回相对应数量的数组，里面存放着num个id
     * @param num
     * @param id
     * @return
     */
    public Long[] findByCountArray(Integer num,Long id){
        Long[] longs=new Long[num];
        for(int i=0;i<num;i++){
            longs[i]=id;
        }
        return longs;
    }

}
