package com.spaceobj.project.dto;

import com.spaceobj.project.group.GetPhoneNumberGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zhr_java@163.com
 * @date 2022/9/9 01:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPhoneNumberDto {

    private long projectId;

    private String userId;

    @NotBlank(message = "UUID为空",
            groups = {GetPhoneNumberGroup.class})
    private String uuid;

}
