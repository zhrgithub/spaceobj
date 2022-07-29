package com.spaceobj.projectHelp.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.projectHelp.mapper.ProjectHelpMapper;
import com.spaceobj.projectHelp.pojo.ProjectHelp;
import com.spaceobj.projectHelp.service.ProjectHelpService;
import org.springframework.stereotype.Service;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
@Service
public class SysProjectHelpServiceImpl extends ServiceImpl<ProjectHelpMapper, ProjectHelp>
    implements ProjectHelpService {

  @Override
  public SaResult createProjectHelpLink(ProjectHelp projectHelp) {

    return null;
  }

  @Override
  public SaResult updateProjectHelpNumber(String projectId) {

    return null;
  }

}
