package com.jeeagile.frame.page;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgilePageUtil {
    /**
     * 开始分页
     *
     * @param list
     * @return
     */
    public static AgilePage startPage(List list, AgilePageable agilePageable) {
        int pageSize = agilePageable.getPageSize();
        int currentPage = agilePageable.getCurrentPage();
        if (currentPage == 0) {
            currentPage = 1;
        }
        AgilePage agilePage = new AgilePage();
        if (list == null || list.isEmpty()) {
            return agilePage;
        }
        int totalCount = list.size();
        int pageCount = 0;
        agilePage.setTotal(list.size());
        agilePage.setSize(pageSize);
        agilePage.setCurrent(currentPage);
        List<String> subList;
        if (totalCount % pageSize > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }
        if (totalCount % pageSize == 0) {
            subList = list.subList((currentPage - 1) * pageSize, pageSize * (currentPage));
        } else {
            if (currentPage == pageCount) {
                subList = list.subList((currentPage - 1) * pageSize, totalCount);
            } else {
                subList = list.subList((currentPage - 1) * pageSize, pageSize * (currentPage));
            }
        }
        agilePage.setPages(pageCount);
        agilePage.setRecords(subList);
        return agilePage;
    }
}
