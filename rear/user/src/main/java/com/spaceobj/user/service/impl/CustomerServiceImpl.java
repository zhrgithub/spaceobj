package com.spaceobj.user.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.user.constent.Resource;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.CustomerUserService;
import com.spaceobj.user.utils.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhr_java@163.com
 * @date 2022/7/23 22:44
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements CustomerUserService {

  @Override
  public SaResult loginOrRegister(
      Integer operateType, String email, String password, String phoneNumber) {

    return null;
  }

  @Override
  public SaResult loginOut() {

    return null;
  }

  @Override
  public SaResult getUserInfo() {

    return null;
  }

  @Override
  public SaResult updateUserInfo(SysUser user) {

    return null;
  }

  @Override
  public SaResult sendMailCode(String account) {

    return null;
  }

  @Override
  public SaResult resetPassword(String account, String emailCode, String newPassword) {

    return null;
  }

  @Override
  public SaResult realName(SysUser user) {

    return null;
  }

  @Override
  public SaResult uploadFile(MultipartFile multipartFile) {

    SaResult saResult = FileUtil.uploadImageFile(multipartFile, Resource.IMAGE_FILE_TYPES);

    if (null == saResult.getData()) {
      return saResult;
    }
    String fileName = (String) saResult.getData();
    System.out.println("fileName: " + fileName);

    String url =
        FileUtil.uploadFileTolocalServer(
            Resource.SYS_USER_ID_CARD_DIRECTORY, fileName, multipartFile);

    return SaResult.ok().setData(url);
  }
}
