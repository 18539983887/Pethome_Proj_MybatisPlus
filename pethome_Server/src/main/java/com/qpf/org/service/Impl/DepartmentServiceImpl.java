package com.qpf.org.service.Impl;

//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.basic.vo.AjaxResult;
import com.qpf.org.dto.DepartmentDto;
import com.qpf.org.mapper.DepartmentMapper;
import com.qpf.org.pojo.Department;
import com.qpf.org.service.IDepartmentService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepartmentServiceImpl implements IDepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void add(Department department) {
        departmentMapper.insert(department);
    }

    @Override
    public void delById(Long id) {

        departmentMapper.deleteById(id);
    }

    @Override
    public void patchDel(Long[] ids) {

        departmentMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void update(Department department) {
        departmentMapper.updateById(department);
    }

    @Override
    public Department findOne(Long id) {
        return departmentMapper.selectById(id);
    }

    @Override
    public List<Department> findAll() {
        return departmentMapper.selectList(null);
    }

    @Override
    public IPage<Department> findByPage(DepartmentDto departmentDto) {
        QueryWrapper<Department> qw = new QueryWrapper<>();
        qw.like("name", departmentDto.getKeyword());
        qw.or();
        qw.like("sn", departmentDto.getKeyword());
        IPage<Department> page = new Page<>(departmentDto.getCurrentPage(), departmentDto.getPageSize());

        page.setRecords(departmentMapper.findByPage(page, qw));
        page.setTotal(departmentMapper.selectCount(qw));
        return page;
    }

    @Override
    public List<Department> getDeptTree() {
        List<Department> deptTree = (List<Department>) redisTemplate.boundValueOps("deptTree").get();
        if(ObjectUtil.isEmpty(deptTree)){
            List<Department> departmentList = departmentMapper.selectList(null);

            Map<Long, Department> map = new HashMap<>();
            for (Department department : departmentList) {
                map.put(department.getId(), department);
            }
            deptTree = new ArrayList<>();
            for (Department department : departmentList) {
                if (department.getParentId() == null) {
                    deptTree.add(department);
                }else {
                    Long parent_id=department.getParentId();
                    Department parentDepartment = map.get(parent_id);

                    parentDepartment.getChildren().add(department);
                }
            }
            redisTemplate.boundValueOps("deptTree").set(deptTree);
            System.out.println("查数据库");
        }else {
            redisTemplate.boundValueOps("deptTree").get();
            System.out.println("查redis缓存");
        }
        return deptTree;

    }
}
