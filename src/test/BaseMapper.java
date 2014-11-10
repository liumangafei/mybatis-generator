package com.demo;

import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description: 所有的Mapper接口类都要继承的该接口
 */
public interface BaseMapper<T, ID extends Serializable> {

    /**
     * 获取所有entity集合
     *
     * @return
     */
    List<T> findAll();

    /**
     * 根据id获取entity
     *
     * @param id
     * @return
     */
    T findById(ID id);

//    /**
//     * 根据ids获取所有的entity集合
//     *
//     * @param ids
//     * @return
//     */
//    List<T> findByIds(List<ID> ids);

    /**
     * 根据entity内的属性值作为条件，查询出符合条件的entity集合
     *
     * @param entity
     * @return
     */
    List<T> findByCondition(T entity);

    /**
     * 获取分页的entity集合
     *
     * @param rowBounds
     * @return
     */
    List<T> findPage(RowBounds rowBounds);

    /**
     * 根据entity内的属性值作为条件，获取符合条件的分页entity集合
     *
     * @param entity
     * @param rowBounds
     * @return
     */
    List<T> findPageByCondition(T entity, RowBounds rowBounds);

    /**
     * 根据entity内的id，更新entity对象
     *
     * @param entity
     * @return
     */
    long updateById(T entity);

    /**
     * 插入一条entity到表中
     *
     * @param entity
     * @return
     */
    T save(T entity);

    /**
     * 统计所有数据的总和
     *
     * @return
     */
    long count();

    /**
     * 根据entity内的属性值作为条件，统计符合条件数据的总和
     *
     * @param entity
     * @return
     */
    long countByCondition(T entity);

    /**
     * 根据id，删除对应的数据
     *
     * @param id
     */
    long deleteById(ID id);

}
