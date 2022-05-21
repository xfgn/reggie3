package com.itheima.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageR<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private List<T> records; //数据

    private long total;//数据总量
    private long pages;//最大页码数
    private long pageSize;//每页数据量
    private long current;//当前页码值

    public PageR (List<T> records, long total, long pages, long pageSize, long current) {
        this.total=total;
        this.pages=pages;
        this.pageSize=pageSize;
        this.current=current;
        this.records = records;
        this.code = 1;
    }

}
