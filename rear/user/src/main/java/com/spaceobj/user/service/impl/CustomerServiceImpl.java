package com.spaceobj.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.CustomerUserService;
import com.spaceobj.user.utils.ResultData;
import org.springframework.stereotype.Service;

/**
 * @author zhr_java@163.com
 * @date 2022/7/23 22:44
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements CustomerUserService {

    @Override
    public ResultData createEmailCode(String account) {

        return null;
    }

    @Override
    public ResultData loginOrRegisterByEmailCode(String phoneNumber, String account, String emailCode) {

        return null;
    }

    @Override
    public ResultData logout(String account) {

        return null;
    }

    @Override
    public ResultData getUserInfo(String account) {

        return null;
    }

    @Override
    public ResultData updateUserInfo(SysUser user) {

        return null;
    }

}
