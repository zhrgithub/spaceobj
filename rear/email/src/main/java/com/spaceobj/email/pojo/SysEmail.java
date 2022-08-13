package com.spaceobj.email.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 11:32
 */
@Data
@TableName(value = "sys_email")
@AllArgsConstructor
@NoArgsConstructor
public class SysEmail implements Serializable {

  @TableId(value = "email_id")
  @NotBlank
  private long emailId;

  @TableField(value = "email_account")
  @NotBlank
  private String emailAccount;

  @TableField(value = "email_password")
  @NotBlank
  private String emailPassword;

  /** 返回账户名称前缀 */
  private String emailAccountName;

  /**
   * 获取账号名称
   *
   * @return
   */
  public String getEmailAccountName() {
    return this.emailAccount.substring(0, this.emailAccount.indexOf("@"));
  }
}
