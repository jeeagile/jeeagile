package com.jeeagile.frame.service;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
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

    /**
     * 初始化数据
     * @return
     */
    default Object initData() {
        return null;
    }

    /**
     * 分页查询
     *
     * @param agilePageable
     * @param queryWrapper
     * @return
     */
    default AgilePage<T> page(AgilePageable<?> agilePageable, Wrapper<T> queryWrapper) {
        AgilePage<T> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        return this.page(agilePage, queryWrapper);
    }

    /**
     * 分页查询
     *
     * @param agilePageable
     * @return
     */
    default AgilePage<T> selectPage(AgilePageable<?> agilePageable) {
        AgilePage<T> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        return this.page(agilePage, queryWrapper((T) agilePageable.getQueryCond()));
    }

    /**
     * 查询列表
     *
     * @return
     */
    default List<T> selectList() {
        return this.list(queryWrapper(null));
    }

    /**
     * 查询列表
     *
     * @param agileModel
     * @return
     */
    default List<T> selectList(T agileModel) {
        return this.list(queryWrapper(agileModel));
    }

    /**
     * 提取查询条件
     *
     * @param agileModel
     * @return
     */
    default AbstractWrapper queryWrapper(T agileModel) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>(agileModel);
        return queryWrapper;
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    default T selectModel(Serializable id) {
        return this.getById(id);
    }

    /**
     * 保存
     *
     * @param agileModel
     * @return
     */
    default T saveModel(T agileModel) {
        this.validateModel(agileModel);
        this.saveModelValidate(agileModel);
        if (!this.save(agileModel)) {
            throw new AgileFrameException("保存数据失败！");
        }
        return agileModel;
    }

    /**
     * 保存校验
     *
     * @param agileModel
     */
    default void saveModelValidate(T agileModel) {
        // default method ignored
    }

    /**
     * 校验数据
     *
     * @param agileModel
     */
    default void validateModel(T agileModel) {
        if (agileModel instanceof AgileValidateModel) {
            ((AgileValidateModel) agileModel).validate();
        }
    }


    /**
     * 更新
     *
     * @param agileModel
     * @return
     */
    default boolean updateModel(T agileModel) {
        this.validateModel(agileModel);
        this.updateModelValidate(agileModel);
        if (this.updateById(agileModel)) {
            return true;
        }
        return false;
    }

    /**
     * 更新校验
     *
     * @param agileModel
     */
    default void updateModelValidate(T agileModel) {
        // default method ignored
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    default boolean deleteModel(Serializable id) {
        this.deleteModelValidate(id);
        this.removeById(id);
        return true;
    }

    /**
     * 删除校验
     *
     * @param id
     * @return
     */
    default void deleteModelValidate(Serializable id) {
        // default method ignored
    }
}
