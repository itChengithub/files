package cn.itchen.ssm.dao;

import cn.itchen.ssm.domain.Traveller;

import java.util.List;

/**
 * 旅客的dao持久层
 */
public interface TravellerDao {
    List<Traveller> findByOid(String oid);
}
