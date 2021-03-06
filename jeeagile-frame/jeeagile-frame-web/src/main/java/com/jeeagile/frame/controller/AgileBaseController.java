package com.jeeagile.frame.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.result.IAgileResultCode;
import com.jeeagile.core.util.spring.AgileServletUtil;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public abstract class AgileBaseController {

    /**
     * 导出数据方便重写
     *
     * @param dataList
     * @param excelName
     */
    protected void exportExcel(List dataList, String excelName, Class<?> entityClass) {
        try {
            HttpServletResponse httpServletResponse = AgileServletUtil.getHttpServletResponse();
            httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            httpServletResponse.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20");
            httpServletResponse.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");
            EasyExcel.write(httpServletResponse.getOutputStream(), entityClass).sheet(excelName).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).doWrite(dataList);
        } catch (Exception ex) {
            throw new AgileFrameException(AgileResultCode.FAIL_OPS_EXPORT, excelName + "数据导出失败！", ex);
        }
    }

    /**
     * 返回成功结果集
     *
     * @return
     */
    protected <T> AgileResult<T> success() {
        return AgileResult.success();
    }

    /**
     * 返回成功结果集
     *
     * @param message 指定返回消息
     * @return
     */
    protected <T> AgileResult<T> success(String message) {
        return AgileResult.success(message);
    }

    /**
     * 返回成功结果集
     *
     * @param data 返回的数据结果集
     * @return
     */
    protected <T> AgileResult<T> success(Object data) {
        return AgileResult.success(data);
    }

    /**
     * 返回成功结果集
     *
     * @param message 指定返回消息
     * @param data    返回的数据结果集
     * @return
     */
    protected <T> AgileResult<T> success(Object data, String message) {
        return AgileResult.success(data, message);
    }

    /**
     * 返回成功结果集
     *
     * @param agileResultCode 返回枚举对象
     * @return
     */
    protected <T> AgileResult<T> agileResult(IAgileResultCode agileResultCode) {
        return AgileResult.agileResult(agileResultCode);
    }

    /**
     * 返回成功结果集
     *
     * @param agileResultCode 返回枚举对象
     * @param data            返回的数据结果集
     * @return
     */
    protected <T> AgileResult<T> agileResult(IAgileResultCode agileResultCode, Object data) {
        return AgileResult.agileResult(agileResultCode, data);
    }

    /**
     * 返回成功结果集
     *
     * @param agileResultCode 返回枚举对象
     * @param data            返回的数据结果集
     * @param message         返回描述
     * @return
     */
    protected <T> AgileResult<T> agileResult(IAgileResultCode agileResultCode, Object data, String message) {
        return AgileResult.agileResult(agileResultCode, data, message);
    }

    /**
     * 指定类型返回结果
     *
     * @param agileResultCode
     * @return
     */
    protected <T> AgileResult<T> error(IAgileResultCode agileResultCode) {
        return AgileResult.error(agileResultCode);
    }

    /**
     * 指定类型返回结果
     *
     * @param agileResultCode
     * @param message
     * @return
     */
    protected <T> AgileResult<T> error(IAgileResultCode agileResultCode, String message) {
        return AgileResult.error(agileResultCode, message);
    }

    /**
     * 异常处理
     *
     * @param agileResultCode
     * @param ex
     * @return
     */
    protected <T> AgileResult<T> error(IAgileResultCode agileResultCode, Exception ex) {
        return AgileResult.error(agileResultCode, ex);
    }

    /**
     * 异常处理 如果异常信息获取不到异常信息则使用默认msg
     *
     * @param agileResultCode
     * @param ex              异常信息
     * @param defaultMessage  默认信息
     * @return
     */
    protected <T> AgileResult<T> error(IAgileResultCode agileResultCode, Exception ex, String defaultMessage) {
        return AgileResult.error(agileResultCode, ex, defaultMessage);
    }

    /**
     * 异常处理
     *
     * @param ex             异常信息
     * @param defaultMessage 默认信息
     * @return
     */
    protected <T> AgileResult<T> error(Exception ex, String defaultMessage) {
        return AgileResult.error(ex, defaultMessage);
    }
}
