package com.spaceobj.user.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.CustomerUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhr_java@163.com
 * @date 2022/7/23 22:44
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements CustomerUserService {

    @Override
    public SaResult loginOrRegister(Integer operateType, String email, String password, String phoneNumber) {

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

        return null;
    }

}
