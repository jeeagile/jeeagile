package com.jeeagile.frame.page;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgilePage<T> implements IPage<T> {

    /**
     * 总页数
     */
    private long pageTotal = 0;
    /**
     * 当前页
     */
    private long currentPage = 1;
    /**
     * 每页显示条数，默认 10
     */
    private long pageSize = 10;
    /**
     * 查询数据列表
     */
    @SuppressWarnings("all")
    @JSONField(name = "recordList")
    private List<T> records = Collections.emptyList();

    /**
     * 排序字段信息
     */
    @Getter
    @Setter
    @JSONField(serialize = false)
    private List<OrderItem> orders = new ArrayList<>();

    /**
     * 自动优化 COUNT SQL
     */
    protected boolean optimizeCountSql = true;
    /**
     * 是否进行 count 查询
     */
    protected boolean searchCount = true;
    /**
     * 是否命中count缓存
     */
    @JSONField(serialize = false)
    protected boolean hitCount = false;
    /**
     * countId
     */
    @Getter
    @Setter
    @JSONField(serialize = false)
    protected String countId;
    /**
     * countId
     */
    @Getter
    @Setter
    @JSONField(serialize = false)
    protected Long maxLimit;

    public AgilePage() {
    }

    /**
     * 分页构造函数
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示条数
     */
    public AgilePage(long currentPage, long pageSize) {
        this(currentPage, pageSize, 0);
    }

    public AgilePage(long currentPage, long pageSize, long pageTotal) {
        this(currentPage, pageSize, pageTotal, true);
    }

    public AgilePage(long currentPage, long pageSize, boolean isSearchCount) {
        this(currentPage, pageSize, 0, isSearchCount);
    }

    public AgilePage(long currentPage, long pageSize, long pageTotal, boolean isSearchCount) {
        if (currentPage > 1) {
            this.currentPage = currentPage;
        }
        this.pageSize = pageSize;
        this.pageTotal = pageTotal;
        this.searchCount = isSearchCount;
    }

    /**
     * 是否存在上一页
     *
     * @return true / false
     */
    public boolean hasPrevious() {
        return this.currentPage > 1;
    }

    /**
     * 是否存在下一页
     *
     * @return true / false
     */
    public boolean hasNext() {
        return this.currentPage < this.getPages();
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public AgilePage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    @JSONField(name = "pageTotal")
    public long getTotal() {
        return this.pageTotal;
    }

    @Override
    public AgilePage<T> setTotal(long pageTotal) {
        this.pageTotal = pageTotal;
        return this;
    }

    @Override
    @JSONField(name = "pageSize")
    public long getSize() {
        return this.pageSize;
    }

    @Override
    public AgilePage<T> setSize(long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    @JSONField(name = "currentPage")
    public long getCurrent() {
        return this.currentPage;
    }

    @Override
    public AgilePage<T> setCurrent(long currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    @Override
    @JSONField(serialize = false)
    public String countId() {
        return getCountId();
    }

    @Override
    @JSONField(serialize = false)
    public Long maxLimit() {
        return getMaxLimit();
    }

    @Override
    @JSONField(serialize = false)
    public List<OrderItem> orders() {
        return getOrders();
    }

    @Override
    @JSONField(serialize = false)
    public boolean optimizeCountSql() {
        return optimizeCountSql;
    }


    @JSONField(serialize = false)
    public boolean searchCount() {
        return this.pageTotal < 0L ? false : this.searchCount;
    }

    public AgilePage<T> setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
        return this;
    }

    public AgilePage<T> setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
        return this;
    }

    public void setHitCount(boolean hit) {
        this.hitCount = hit;
    }

    @Override
    @JSONField(serialize = false)
    public long getPages() {
        if (this.getSize() == 0L) {
            return 0L;
        } else {
            long pages = this.getTotal() / this.getSize();
            if (this.getTotal() % this.getSize() != 0L) {
                ++pages;
            }
            return pages;
        }
    }
}
