package com.spaceobj.project.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.domain.SysProject;
import com.spaceobj.project.bo.GetPhoneNumberBo;
import com.spaceobj.project.bo.ProjectSearchBo;
import com.spaceobj.project.dto.GetPhoneNumberDto;
import com.spaceobj.project.dto.ProjectSearchDto;
import com.spaceobj.project.dto.SysProjectDto;
import com.spaceobj.project.group.AddPageViewsGroup;
import com.spaceobj.project.group.AuditProjectGroup;
import com.spaceobj.project.group.InsertProjectGroup;
import com.spaceobj.project.group.UpdateProjectGroup;
import com.spaceobj.project.service.SysProjectService;
import com.spaceobj.project.util.BeanConvertToTargetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhr_java@163.com
 * @date 2022/9/5 22:11
 */
@RestController(value = "projectController")
@RequestMapping(value = "project", method = RequestMethod.POST)
public class ProjectController {

  @Autowired private SysProjectService sysProjectService;

  @PostMapping(value = "addProject")
  public SaResult addProject(@Validated(InsertProjectGroup.class) SysProjectDto sysProjectDto) {

    SysProject sysProject = SysProject.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(sysProjectDto, sysProject);

    return sysProjectService.addProject(sysProject);
  }

  @PostMapping(value = "updateProject")
  public SaResult updateProject(@Validated(UpdateProjectGroup.class) SysProjectDto sysProjectDto) {

    SysProject sysProject = SysProject.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(sysProjectDto, sysProject);

    return sysProjectService.updateProject(sysProject);
  }

  @PostMapping(value = "auditProject")
  public SaResult auditProject(@Validated(AuditProjectGroup.class) SysProjectDto sysProjectDto) {

    SysProject sysProject = SysProject.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(sysProjectDto, sysProject);

    return sysProjectService.auditProject(sysProject);
  }

  @PostMapping("findList")
  public SaResult findList(@Validated ProjectSearchDto projectSearchDto) {

    ProjectSearchBo projectSearchBo = ProjectSearchBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(projectSearchDto, projectSearchBo);
    return sysProjectService.findList(projectSearchBo);
  }

  @PostMapping(value = "addPageViews")
  public void addPageViews(@Validated(AddPageViewsGroup.class) SysProjectDto sysProjectDto) {
    sysProjectService.addPageViews(sysProjectDto.getPId());
  }

  @PostMapping("getPhoneNumberByProjectId")
  public SaResult getPhoneNumberByProjectId(@Validated GetPhoneNumberDto getPhoneNumberDto) {
    GetPhoneNumberBo getPhoneNumberBo = GetPhoneNumberBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(getPhoneNumberDto, getPhoneNumberBo);
    return sysProjectService.getPhoneNumberByProjectId(getPhoneNumberBo);
  }
}
