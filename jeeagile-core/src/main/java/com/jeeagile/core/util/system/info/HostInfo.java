package com.jeeagile.core.util.system.info;

import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 主机信息
 */
@Data
public class HostInfo {

    /**
     * 主机名称
     */
    private String name;

    /**
     * 主机地址
     */
    private String address;
}
