package com.qpf.basic.utils;

import com.qpf.basic.exception.SystemException;
import com.qpf.org.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailMessageUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    //获取邮件发件人
    @Value("${spring.mail.username}")
    private String mimeMessageFrom;

    /**
     * 发送店铺审核结果邮件
     *
     * @param toEmail    收件人
     * @param shopId     店铺Id
     * @param censorType 审核结果（通过：true，驳回：false）
     */
    public void sendCensorShopMail(String toEmail, Long shopId, boolean censorType) {
        try {
            //1.创建邮件客户端
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //2.创建邮件信息对象
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            //3.添加发件人
            helper.setFrom(mimeMessageFrom);
            //4.添加标题
            helper.setSubject("【宠物之家】店铺审核结果");
            //5.根据审核结果，发送不同邮件内容
            if (censorType) {
                //5.1 审核通过，需要激活
                helper.setText("<h3>你的店铺已经审核通过，请<a style='color:green' href='http://127.0.0.1:8080/shop/active/" + shopId + "'>点击这里</a>激活邮件</h3>", true);
            } else {
                //5.2 审核失败，需要重新修改信息
                helper.setText("<h3>你的店铺审核未通过，请<a style='color:red' href='http://127.0.0.1:8081/#/regiseterEdit/" + shopId
                        + "'>点击这里</a>修改信息重新提交</h3>", true);
            }
            //6.添加收件人
            helper.setTo(toEmail);

            //7.发送邮件
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new SystemException("发送店铺审核结果邮件失败！");
        }
    }

    /**
     * 发送寻主消息审核结果邮件
     *
     * @param toEmail 收件人
     * @param msgTitle   寻主消息标题
     * @param censorType   审核结果（通过：true，驳回：false）
     */
    public void sendCensorSMMail(String toEmail, String msgTitle, boolean censorType) {
        try {
            //1.创建邮件客户端
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //2.创建邮件信息对象
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            //3.添加发件人
            helper.setFrom(mimeMessageFrom);
            //4.添加标题
            helper.setSubject("【宠物之家】寻主消息审核结果");
            //5.根据审核结果，发送不同邮件内容
            if (censorType) {
                //5.1 审核通过，需要激活
                helper.setText("<h3>你发布《" + msgTitle + "》寻主消息已经审核通过，在1-2个工作日内会有专门的工作人员和您联系，请保持您的电话畅通！", true);
            } else {
                //5.2 审核失败，需要重新修改信息
                helper.setText("<h3>你发布《" + msgTitle + "》寻主消息审核未通过，请检查并修改您所发布的寻主消息!", true);
            }

            //6.添加收件人
            helper.setTo(toEmail);

            //7.发送邮件
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new SystemException("发送寻主消息审核结果邮件失败！");
        }
    }
}