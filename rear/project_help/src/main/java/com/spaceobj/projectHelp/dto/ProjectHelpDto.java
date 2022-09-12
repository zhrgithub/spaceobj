package com.spaceobj.projectHelp.dto;

import com.spaceobj.projectHelp.group.InsertProjectHelpGroup;
import com.spaceobj.projectHelp.group.UpdateProjectHelpGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
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

  @NotNull(
      message = "项目ID是必填项",
      groups = {InsertProjectHelpGroup.class})
  @Min(1)
  private Long pId;

  private Long hpNumber;

  private String pContent;

  private BigDecimal pPrice;

  private String pReleaseUserId;

  private Integer hpStatus;
}
