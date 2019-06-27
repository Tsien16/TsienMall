package com.tsien.mall.controller.protal;

import com.tsien.mall.constant.Const;
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
 * @date 2019/6/23 0023 12:19
 */

@RestController
@RequestMapping("/user/")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登陆
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
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    /**
     * 退出登陆，原理：从session里移除用户数据
     *
     * @param session session
     * @return 删除成功的信息
     */
    @PostMapping("logout.do")
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     *
     * @param userDO 用户实体
     * @return 注册结果
     */
    @PostMapping("register.do")
    public ServerResponse<String> register(UserDO userDO) {
        return userService.register(userDO);
    }

    /**
     * 检查用户名或者邮箱的合法性
     *
     * @param string string
     * @param type   email/username
     * @return 检查结果
     */
    @PostMapping("check_valid.do")
    public ServerResponse<String> checkValid(String string, String type) {
        return userService.checkValid(string, type);
    }


}
