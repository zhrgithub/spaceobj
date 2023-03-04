package com.spaceobj.project.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 15:28
 */
@Data
public class KafkaMessage {

    private Long id;

    private String msg;

    private Date sendTime;

}
