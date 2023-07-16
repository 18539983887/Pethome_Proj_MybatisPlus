package com.qpf.user.service;

import com.qpf.user.pojo.User;
import com.qpf.user.dto.UserDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IUserService {
    /**
     * 添加一个对象
     * @param user
     */
    void add(User user);

    /**
     * 删除一个对象
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void patchDel(List<Long> ids);

    /**
     * 更新一个对象
     * @param user
     */
    void updateById(User user);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    User findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<User> findAll();

    /**
     * 分页查询
     * @param  userDto 分页参数
     * @return
     */
    IPage<User> findByPage(UserDto userDto);

    void phoneRegister(UserDto userDto);
}
