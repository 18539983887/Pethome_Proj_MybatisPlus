package com.qpf.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.system.annotion.PreAuthorize;
import com.qpf.system.utils.ClassUtils;
import com.qpf.system.utils.RestUriUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.system.pojo.Permission;
import com.qpf.system.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.system.mapper.PermissionMapper;
import com.qpf.system.dto.PermissionDto;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 业务实现类：
 */
@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public void add(Permission permission){
        permissionMapper.insert(permission);
    }

    @Override
    public void deleteById(Long id){
        permissionMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        permissionMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(Permission permission){
        permissionMapper.updateById(permission);
    }

    @Override
    public Permission findOne(Long id){
        return permissionMapper.selectById(id);
    }

    @Override
    public List<Permission>findAll(){
        return permissionMapper.selectList(null);
    }

    @Override
    public IPage<Permission>findByPage(PermissionDto permissionDto){
        //1.创建查询条件
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        //qw.like("xxx",permissionDto.getXxx());
        //qw.or();
        //qw.like("xxx",permissionDto.getXxx());
        //2.组件分页数据
        IPage<Permission> page = new Page<>(permissionDto.getCurrentPage(), permissionDto.getPageSize());
        page.setRecords(permissionMapper.findByPage(page,qw));
        page.setTotal(permissionMapper.selectCount(qw));
        //3.返回
        return page;
    }

    private static final String PKG_PREFIX = "com.qpf.";
    private static final String PKG_SUFFIX = ".controller";


    @Override
    public void scanPermission() {
        //获取cn.itsource下面所有的模块目录
        String path = this.getClass().getResource("/").getPath() + "/com/qpf/";
        File file = new File(path);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            //只要目录
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });

        //获取cn.itsource.*.controller里面所有的类
        Set<Class> clazzes = new HashSet<>();
        for (File fileTmp : files) {
            System.out.println("===============权限注解解析：获取所有的包==============");
            System.out.println(fileTmp.getName());
            clazzes.addAll(ClassUtils.getClasses(PKG_PREFIX + fileTmp.getName() + PKG_SUFFIX));
        }


        //遍历所有controller包下的所有类【字节码】
        for (Class clazz : clazzes) {
            //获取当前类中的所有方法
            Method[] methods = clazz.getMethods();
            //如果controller中没有方法 - 直接结束
            if (methods == null || methods.length < 1)
                return;
            for (Method method : methods) {
                //获取url：1个数据
                String uri = RestUriUtil.getUri(clazz, method);
                try {
                    //从方法上去获取PreAuthorize注解
                    PreAuthorize preAuthorizeAnno = method.getAnnotation(PreAuthorize.class);
                    if (preAuthorizeAnno == null) {
                        continue;
                    }
                    //获取PreAuthorize注解上的name属性值
                    String name = preAuthorizeAnno.name();
                    //获取PreAuthorize注解上的sn属性值
                    String permissionSn = preAuthorizeAnno.sn();
                    //通过sn去查询数据库
                    QueryWrapper<Permission> qw = new QueryWrapper<>();
                    qw.eq("sn", permissionSn);
                    Permission permissionTmp = permissionMapper.selectOne(qw);
                    //如果不存在就添加
                    if (permissionTmp == null) { //添加
                        Permission permission = new Permission();
                        permission.setName(name);       //t_permission表中的权限名
                        permission.setSn(permissionSn); //t_permission表中的权限编号
                        permission.setUrl(uri);           //t_permission表中的权限路径
                        permissionMapper.insert(permission);
                    } else {
                        //如果存在就修改
                        permissionTmp.setName(name);
                        permissionTmp.setUrl(uri);
                        permissionMapper.updateById(permissionTmp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
