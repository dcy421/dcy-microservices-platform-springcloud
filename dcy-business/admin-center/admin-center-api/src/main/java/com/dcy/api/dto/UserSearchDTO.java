package com.dcy.api.dto;

import com.dcy.db.base.model.PageModel;
import lombok.Data;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-17 13:22
 */
@Data
public class UserSearchDTO extends PageModel {
    private String username;
}
