package com.jeeagile.frame.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.frame.entity.AgileModel;
import com.jeeagile.frame.entity.AgileValidateModel;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;

import java.io.Serializable;
import java.util.List;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileService<T extends AgileModel> extends IService<T> {
    default AgilePage<T> page(AgilePageable<?> agilePageable, Wrapper<T> queryWrapper) {
        AgilePage<T> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        return this.page(agilePage, queryWrapper);
    }

    default AgilePage<T> selectPage(AgilePageable<?> agilePageable) {
        AgilePage<T> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        return this.page(agilePage, getQueryWrapper((T) agilePageable.getQueryCond()));
    }

    default List<T> selectList(T entity) {
        return this.list(getQueryWrapper(entity));
    }

    default QueryWrapper<T> getQueryWrapper(T entity) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>(entity);
        return queryWrapper;
    }

    default T saveEntity(T entity) {
        this.saveBefore(entity);
        if (this.save(entity)) {
            this.saveAfter(entity);
        } else {
            throw new AgileFrameException("保存数据失败！");
        }
        return entity;
    }

    default void validateModel(T entity) {
        if (entity instanceof AgileValidateModel) {
            ((AgileValidateModel) entity).validate();
        }
    }

    /**
     * 保存前操作
     *
     * @param entity
     */
    default void saveBefore(T entity) {
        this.validateModel(entity);
    }


    /**
     * 保存后操作
     */
    default void saveAfter(T entity) {
        // default method ignored
    }

    default boolean updateEntity(T entity) {
        this.updateBefore(entity);
        if (this.updateById(entity)) {
            this.updateAfter(entity);
            return true;
        }
        return false;
    }


    /**
     * 更新前操作
     *
     * @param entity
     */
    default void updateBefore(T entity) {
        this.validateModel(entity);
    }

    /**
     * 更新后操作
     */
    default void updateAfter(T entity) {
        // default method ignored
    }

    default boolean deleteById(Serializable id) {
        this.deleteBefore(id);
        this.removeById(id);
        this.deleteAfter(id);
        return true;
    }

    /**
     * 删除前操作
     *
     * @param id
     */
    default void deleteBefore(Serializable id) {
        // default method ignored
    }

    /**
     * 删除后操作
     */
    default void deleteAfter(Serializable id) {
        // default method ignored
    }

}
