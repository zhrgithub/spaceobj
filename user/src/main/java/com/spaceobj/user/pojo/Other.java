package com.spaceobj.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 22:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Other implements Serializable {
  /** 客服微信 */
  @NotBlank(message = "客服微信不为空")
  private String wechat;

  /** 下载链接 */
  @NotBlank(message = "下载链接不为空")
  private String downloadUrl;

  /** 上线开关 */
  @NotNull(message = "上线开关不为空")
  private Integer online;

  /** 版本号,控制APP端审核版本是否通过，审核通过，并发布成功之后升级版本号 */
  @NotNull(message = "上线版本号不为空")
  private Integer version;
}
