package com.qpf.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.basic.dto.PointDto;
import com.qpf.basic.exception.BusinessException;
import com.qpf.basic.service.impl.FastDfsServiceImpl;
import com.qpf.basic.utils.*;
import com.qpf.org.mapper.ShopMapper;
import com.qpf.org.pojo.Shop;
import com.qpf.basic.constant.SearchMasterMsgState;
import com.qpf.pet.mapper.SearchMasterMsgAuditLogMapper;
import com.qpf.pet.pojo.SearchMasterMsgAuditLog;

import com.qpf.user.mapper.UserMapper;
import com.qpf.user.pojo.Logininfo;
import com.qpf.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.pet.pojo.SearchMasterMsg;
import com.qpf.pet.service.ISearchMasterMsgService;
import com.qpf.pet.mapper.SearchMasterMsgMapper;
import com.qpf.pet.dto.SearchMasterMsgDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class SearchMasterMsgServiceImpl implements ISearchMasterMsgService {

    @Autowired
    private SearchMasterMsgMapper searchMasterMsgMapper;
    @Autowired
    private FastDfsServiceImpl fastDfsService;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailMessageUtils mailMessageUtils;
    @Autowired
    private SearchMasterMsgAuditLogMapper searchMasterMsgAuditLogMapper;



    @Override
    public void add(SearchMasterMsg searchMasterMsg) {
        searchMasterMsgMapper.insert(searchMasterMsg);
    }

    @Override
    public void deleteById(Long id) {
        searchMasterMsgMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids) {
        searchMasterMsgMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(SearchMasterMsg searchMasterMsg) {
        searchMasterMsgMapper.updateById(searchMasterMsg);
    }

    @Override
    public SearchMasterMsg findOne(Long id) {
        return searchMasterMsgMapper.selectById(id);
    }

    @Override
    public List<SearchMasterMsg> findAll() {
        return searchMasterMsgMapper.selectList(null);
    }

    @Override
    public IPage<SearchMasterMsg> findByPage(SearchMasterMsgDto searchMasterMsgDto) {
        //1.创建查询条件
        QueryWrapper<SearchMasterMsg> qw = new QueryWrapper<>();
        //qw.like("xxx",searchMasterMsgDto.getXxx());
        //qw.or();
        //qw.like("xxx",searchMasterMsgDto.getXxx());
        //2.组件分页数据
        IPage<SearchMasterMsg> page = new Page<>(searchMasterMsgDto.getCurrentPage(), searchMasterMsgDto.getPageSize());
        page.setRecords(searchMasterMsgMapper.findByPage(page, qw));
        page.setTotal(searchMasterMsgMapper.selectCount(qw));
        //3.返回
        return page;
    }

    @Override
    public void publish(SearchMasterMsg searchMasterMsg, HttpServletRequest request) {
        Boolean aBoolean = BaiduAuditUtils.TextCensor(searchMasterMsg.getTitle());
        if (!aBoolean) {
            throw new BusinessException("标题违规,请检查后重试!");
        }
        String resources = searchMasterMsg.getResources();
        System.out.println(resources);
        System.out.println("-------------------");
        String[] imgUrls = resources.split(",");
        for (String imgUrl : imgUrls) {
            byte[] bytes = fastDfsService.downloadFile(imgUrl);
            Boolean aBooleanImgCensor = BaiduAuditUtils.ImgCensor(bytes);
            if (!aBooleanImgCensor) {
                throw new BusinessException("图片违规,请检查后重试!");
            }
        }
        System.out.println(imgUrls);
        //获取登录人id
        Logininfo logininfo = LoginContext.getLogininfo(request);
        userMapper.selectOne(new QueryWrapper<User>().eq("logininfo_id",logininfo.getId()));

        //3.找到最近的店铺 - 分配店铺【设置到shopId】 - 发送短信给店铺管理员，告诉他去接回宠物
        //3.1 计算自身坐标
        PointDto pointDto=BaiduMapUtils.getPoint(searchMasterMsg.getAddress());
        Shop nearestShop = ShopNearestUtils.getNearestShop(pointDto, shopMapper.selectList(null));
        if(nearestShop!=null){
            searchMasterMsg.setState(SearchMasterMsgState.ToAudit);
            searchMasterMsg.setShopId(nearestShop.getId());
        }else {
            searchMasterMsg.setState(SearchMasterMsgState.SearchPool);
        }

    }

    @Override
    public IPage<SearchMasterMsg> toAudit(SearchMasterMsgDto searchMasterMsgDto) {
        //1.创建查询条件
        QueryWrapper<SearchMasterMsg> qw = new QueryWrapper<>();
        qw.eq("state",SearchMasterMsgState.ToAudit);
        //2.组件分页数据
        IPage<SearchMasterMsg> page = new Page<>(searchMasterMsgDto.getCurrentPage(), searchMasterMsgDto.getPageSize());
        page.setRecords(searchMasterMsgMapper.findByPage(page, qw));
        page.setTotal(searchMasterMsgMapper.selectCount(qw));
        //3.返回
        return page;
    }

    @Override
    public void auditResult(SearchMasterMsgAuditLog searchMasterMsgAuditLog) {

            //1.查询该"寻主消息"所属用户
            SearchMasterMsg searchMasterMsg = searchMasterMsgMapper.selectById(searchMasterMsgAuditLog.getMsgId());
            User user = userMapper.selectById(searchMasterMsg.getUserId());
            String email = user.getEmail();
            //2.判断状态
            if (searchMasterMsgAuditLog.getState() == 0){
                //2.1 审核->驳回
                //① 修改寻主信息的状态, 修改为-1
                searchMasterMsg.setState(SearchMasterMsgState.RejectAudit);
                //② 发邮件
                mailMessageUtils.sendCensorSMMail(email,searchMasterMsg.getTitle(),false);
                //③ 发短信提醒(暂时无法实现,需要重新申请短信模版)
                // String phone = user.getPhone();
                // AliyunSmsUtils.sendSmsCaptcha(phone,"");
            }else{
                //2.2 审核->通过
                //① 修改寻主信息的状态. 其实修改为1
                searchMasterMsg.setState(SearchMasterMsgState.PassAudit);
                //② 发邮件
                mailMessageUtils.sendCensorSMMail(email,searchMasterMsg.getTitle(),true);
                //③ 发短信提醒(暂时无法实现,需要重新申请短信模版)
                // String phone = user.getPhone();
                // AliyunSmsUtils.sendSmsCaptcha(phone,"");
            }
            //3.修改寻主信息的状态->保存到数据库
            searchMasterMsgMapper.updateById(searchMasterMsg);

            //4.存储审核日志
            searchMasterMsgAuditLogMapper.insert(searchMasterMsgAuditLog);

    }
}
