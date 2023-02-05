package com.spaceobj.user.constant;

public interface Resource {

  /** 系统用户身份证路径 */
  String SYS_USER_ID_CARD_DIRECTORY = "/opt/spaceobj/idCardPic/";

  /** 域名 */
  String DOMAIN_NAME = "https://www.spaceobj.com/idcardPicture/";

  /** 上传图片文件类型格式 */
  String[] IMAGE_FILE_TYPES = {
    ".jpg", ".jpeg",
    ".png", ".bmp",
    ".gif"
  };

  /** 上传视频文件类型格式 */
  String[] VIDEO_FILE_TYPES = {
    ".wav", ".avi",
    ".mkv", ".mp4",
    ".mov", ".mpeg"
  };
}
