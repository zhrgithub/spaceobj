package com.spaceobj.project.dto;

import com.spaceobj.project.group.InsertProjectHelpGroup;
import com.spaceobj.project.group.QueryProjectHelpListGroup;
import com.spaceobj.project.group.UpdateProjectHelpGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zhr
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectHelpDto {

  @NotBlank(
      message = "项目助力ID是必填项",
      groups = {UpdateProjectHelpGroup.class})
  private String hpId;

  @NotBlank(
      message = "项目UUID是必填项",
      groups = {InsertProjectHelpGroup.class})
  private String pUUID;

  private Long hpNumber;

  private String pContent;

  private BigDecimal pPrice;

  private String pReleaseUserId;

  private Integer hpStatus;


  /** 当前页 */
  @NotNull(message = "当前页非空",groups = {QueryProjectHelpListGroup.class})
  private Integer currentPage;

  /** 每页条数 */
  @NotNull(message = "每页条数条数非空",groups = {QueryProjectHelpListGroup.class})
  private Integer pageSize;

  @DateTimeFormat(pattern="yyy-MM-dd HH:mm:ss")
  private Data createTime;

  @DateTimeFormat(pattern="yyy-MM-dd HH:mm:ss")
  private Data updateTime;


}
