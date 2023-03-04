package com.spaceobj.user.dto;

import com.spaceobj.user.group.photo.AddOrUpdatePhotoGroup;
import com.spaceobj.user.group.photo.DeletePhotoGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhr
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPhotoDto implements Serializable {

    /** 图片id */
    @NotNull(message = "图片id不为空",
            groups = {DeletePhotoGroup.class})
    private String photoId;

    /** 图片URl */
    @NotBlank(message = "图片URL不为空",
            groups = {AddOrUpdatePhotoGroup.class})
    private String photoUrl;

    /** 操作类型 */
    @NotNull(message = "操作类型不为空",
            groups = {AddOrUpdatePhotoGroup.class})
    private Integer operation;

}
