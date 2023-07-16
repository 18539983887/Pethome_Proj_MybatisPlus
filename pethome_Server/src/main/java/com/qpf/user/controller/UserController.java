package com.qpf.user.controller;

import com.qpf.user.service.IUserService;
import com.qpf.user.pojo.User;
import com.qpf.user.dto.UserDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public IUserService userService;

    /**
     * 接口：添加或修改
     * @param user 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody User user) {
        if ( user.getId() != null){
            userService.updateById(user);
        }else{
            userService.add(user);
        }
        return new AjaxResult();

    }

    /**
    * 接口：删除
    * @param id
    * @return AjaxResult 响应给前端
    */
    @DeleteMapping(value = "/{id}")
    public AjaxResult del(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        userService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public User findOne(@PathVariable("id") Long id) {
        return userService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param userDto 查询对象
     * @return IPage<User> 分页对象
     */
    @PostMapping
    public IPage<User> findByPage(@RequestBody UserDto userDto) {
        return userService.findByPage(userDto);
    }
    //用户注册
    @PostMapping("/register/phone")
    public AjaxResult phoneRegister(@RequestBody UserDto userDto) {
        userService.phoneRegister(userDto);
        return new AjaxResult();
    }
}
