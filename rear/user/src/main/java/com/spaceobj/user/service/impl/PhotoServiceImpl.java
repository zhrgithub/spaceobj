package com.spaceobj.user.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.user.mapper.SysPhotoMapper;
import com.spaceobj.user.pojo.SysPhoto;
import com.spaceobj.user.service.PhotoService;
import org.springframework.stereotype.Service;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 00:28
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<SysPhotoMapper, SysPhoto> implements PhotoService {

    @Override
    public SaResult addOrUpdate(SysPhoto photo, Integer operation) {

        return null;
    }

    @Override
    public SaResult delete(Long id) {

        return null;
    }

    @Override
    public SaResult photoList() {

        return null;
    }

}
