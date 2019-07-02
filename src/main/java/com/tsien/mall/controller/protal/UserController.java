package com.tsien.mall.controller.protal;

import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.ResponseCodeEnum;
import com.tsien.mall.model.UserDO;
import com.tsien.mall.service.UserService;
import com.tsien.mall.util.ServerResponse;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/user")
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
    public ServerResponse<UserDO> login(HttpSession session,
                                        @RequestParam("username") String username,
                                        @RequestParam("password") String password) {

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
    public ServerResponse<String> checkValid(@RequestParam("string") String string,
                                             @RequestParam("type") String type) {
        return userService.checkValid(string, type);
    }

    /**
     * 获取用户信息，未登录不强制登陆
     *
     * @param session session
     * @return 用户信息
     */
    @PostMapping("get_userInfo.do")
    public ServerResponse<UserDO> getUserInfo(HttpSession session) {
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO != null) {
            return ServerResponse.createBySuccess(userDO);
        }

        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前的用户信息");
    }

    /**
     * 获取忘记密码提示问题
     *
     * @param username username
     * @return 忘记密码提示问题
     */
    @PostMapping("get_forget_password_prompts.do")
    public ServerResponse<String> getForgetPasswordPrompts(@RequestParam("username") String username) {
        return userService.getQuestion(username);
    }

    /**
     * 检查问题的答案
     *
     * @param username username
     * @param question question
     * @param answer   answer
     * @return 验证结果
     */
    @PostMapping("check_answer.do")
    public ServerResponse<String> checkAnswer(@RequestParam("username") String username,
                                              @RequestParam("question") String question,
                                              @RequestParam("answer") String answer) {
        return userService.checkAnswer(username, question, answer);

    }

    /**
     * 未登录状态下的修改密码
     *
     * @param username    username
     * @param newPassword newPassword
     * @param forgetToken forgetToken
     * @return 修改密码的结果
     */
    @PostMapping("forget_reset_password.do")
    public ServerResponse<String> forgetResetPassword(@RequestParam("username") String username,
                                                      @RequestParam("newPassword") String newPassword,
                                                      @RequestParam("forgetToken") String forgetToken) {
        return userService.forgetResetPassword(username, newPassword, forgetToken);

    }

    /**
     * 登陆状态下重置密码
     *
     * @param session     session
     * @param oldPassword oldPassword
     * @param newPassword newPassword
     * @return 重置的结果
     */
    @PostMapping("reset_password.do")
    public ServerResponse<String> resetPassword(HttpSession session,
                                                @RequestParam("oldPassword") String oldPassword,
                                                @RequestParam("newPassword") String newPassword) {

        // 判断用户登陆状态
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        return userService.resetPassword(userDO, oldPassword, newPassword);

    }

    /**
     * 获取用户信息，如果未登陆强制登陆
     *
     * @param session session
     * @return 用户信息
     */
    @PostMapping("get_userInfo_of_need_login.do")
    public ServerResponse<UserDO> getUserInfoOfNeedLogin(HttpSession session) {

        // 判断用户登陆状态
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录,需要强制登陆");
        }

        return userService.getUserInfo(userDO.getId());

    }

    /**
     * 更新个人用户信息
     *
     * @param session session
     * @param userDO  userDO
     * @return 更新的结果
     */
    @PostMapping("update_userInfo.do")
    public ServerResponse<UserDO> updateUserInfo(HttpSession session, UserDO userDO) {

        // 判断用户登陆状态
        UserDO currentUser = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        // userDo里没userId
        userDO.setId(currentUser.getId());

        ServerResponse<UserDO> response = userService.updateUserInfo(userDO);

        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;

    }

}
