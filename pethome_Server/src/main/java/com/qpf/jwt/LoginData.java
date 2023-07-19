package com.qpf.jwt;

import com.qpf.system.pojo.Menu;
import com.qpf.user.pojo.Logininfo;
import lombok.Data;

import java.util.List;

@Data
public class LoginData {
    //1.登录信息对象Lonininfo对象 - 在前端显示用户数据信息的【
    private Logininfo logininfo;
    //2.当前登录人的所有权限的sn - 按钮或资源权限【没有访问该资源的按钮直接不显示】
    private List<String> permissions;
    //3.当前登录人的菜单信息 - 菜单权限【不同的人登录之后菜单是不一样的】
    private List<Menu> menus;
}