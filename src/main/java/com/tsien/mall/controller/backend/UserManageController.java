package com.tsien.mall.controller.backend;

import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.RoleEnum;
import com.tsien.mall.model.UserDO;
import com.tsien.mall.service.UserService;
import com.tsien.mall.util.ServerResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/28 0028 0:20
 */

@RestController
@RequestMapping("/manage/user")
public class UserManageController {

    @Resource
    private UserService userService;

    /**
     * 管理员用户登陆
     *
     * @param session  session
     * @param username username
     * @param password password
     * @return response
     */
    @PostMapping("login.do")
    public ServerResponse<UserDO> login(HttpSession session, String username, String password) {

        ServerResponse<UserDO> response = userService.login(username, password);
        if (response.isSuccess()) {
            UserDO userDO = response.getData();

            // 判断登陆的用户是否是管理员
            if (userDO.getRole() == RoleEnum.ROLE_ADMIN.getCode()) {
                session.setAttribute(Const.CURRENT_USER, response.getData());
                return response;
            } else {
                return ServerResponse.createByErrorMessage("不是管理员，无法登陆");
            }
        }

        return response;

    }


}
