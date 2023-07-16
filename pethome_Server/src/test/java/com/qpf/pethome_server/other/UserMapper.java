package com.qpf.pethome_server.other;

import com.qpf.user.pojo.User;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface UserMapper {
    List<User> getUserList(RowBounds rowBounds);
}