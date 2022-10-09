package com.spaceobj.projectHelp.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.projectHelp.bo.ProjectHelpBo;
import com.spaceobj.projectHelp.dto.ProjectHelpDto;
import com.spaceobj.projectHelp.group.InsertProjectHelpGroup;
import com.spaceobj.projectHelp.group.QueryProjectHelpListGroup;
import com.spaceobj.projectHelp.group.UpdateProjectHelpGroup;
import com.spaceobj.projectHelp.service.ProjectHelpService;
import com.spaceobj.projectHelp.util.BeanConvertToTargetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhr_java@163.com
 * @date 2022/9/7 01:05
 */
@RestController("projectHelpController")
@RequestMapping(value = "projectHelp", method = RequestMethod.POST)
public class ProjectHelpController {

  @Autowired private ProjectHelpService projectHelpService;

  @PostMapping("createProjectHelpLink")
  public SaResult createProjectHelpLink(
      @Validated(InsertProjectHelpGroup.class) ProjectHelpDto projectHelpDto) {
    ProjectHelpBo projectHelpBo = ProjectHelpBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(projectHelpDto, projectHelpBo);
    return projectHelpService.createProjectHelpLink(projectHelpBo);
  }

  @PostMapping("updateProjectHelpNumber")
  public SaResult updateProjectHelpNumber(
      @Validated(UpdateProjectHelpGroup.class) ProjectHelpDto projectHelpDto) {
    ProjectHelpBo projectHelpBo = ProjectHelpBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(projectHelpDto, projectHelpBo);
    return projectHelpService.updateProjectHelpNumber(projectHelpBo);
  }

  @PostMapping("projectHelpList")
  public SaResult projectHelpList(
      @Validated(QueryProjectHelpListGroup.class) ProjectHelpDto projectHelpDto) {
    ProjectHelpBo projectHelpBo = ProjectHelpBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(projectHelpDto, projectHelpBo);
    return projectHelpService.projectHelpList(projectHelpBo);
  }

}
