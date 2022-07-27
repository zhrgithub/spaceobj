package com.spaceobj.user.utils;

import cn.dev33.satoken.util.SaResult;

import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class FileUtil {

  /** 构造文件名 */
  public static String generateFileName() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    String date = dateFormat.format(new Date());
    return date + "-" + UUID.randomUUID().toString();
  }

  /**
   * 上传文件到本地服务器
   *
   * @param fileDirectory
   * @param fileName
   * @param coverFile
   * @return
   */
  public static String uploadFileTolocalServer(
      String fileDirectory, String fileName, MultipartFile coverFile) {
    try {
      File directory = new File(fileDirectory);
      if (!directory.exists()) {
        directory.mkdirs();
      }
      // 本地服务器文件路径
      File dest = new File(fileDirectory + fileName);
      if (!dest.exists()) {
        dest.createNewFile();
      }
      coverFile.transferTo(dest);
      String url = fileDirectory + fileName;
      return url;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /** 上传文件 */
  public static SaResult uploadImageFile(MultipartFile uploadFile, String[] fileTypes) {
    if (ObjectUtils.isEmpty(uploadFile)) {
      return SaResult.error("请求参数错误");
    }
    // 获取文件源文件名称
    String originalFilename = uploadFile.getOriginalFilename();
    // 文件后缀名，格式：.*
    String suffixName = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
    if (!Arrays.asList(fileTypes).contains(suffixName)) {
      return SaResult.error("不支持此文件类型");
    }
    // 构造文件名
    String fileName = generateFileName() + suffixName;
    return SaResult.ok().setData(fileName);
  }
}
