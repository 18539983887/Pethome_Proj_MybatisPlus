package com.qpf.org.service;

//import com.github.pagehelper.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qpf.basic.vo.AjaxResult;
import com.qpf.org.dto.DepartmentDto;
import com.qpf.org.pojo.Department;

import java.util.List;

public interface IDepartmentService {
    void add(Department department);
    void delById(Long id);
    void patchDel(Long[] ids);
    void update(Department department);
    Department findOne(Long id);
    List<Department>findAll();
    IPage<Department> findByPage(DepartmentDto departmentDto);

    List<Department> getDeptTree();
}
