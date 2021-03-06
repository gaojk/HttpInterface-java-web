package cn.qlk.test.until;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import sun.misc.BASE64Encoder;

public class SendMailUntil {

	 final static String content = "<b><font color="+"#0B610B"+">该邮件为自动发送模板，非相关人员可忽略</font></b> "
			+ "<hr size="+"2"+" width="+"100%"+" align="+"center"+" /><li>项目名称：接口测试平台接口用例</li> <li>发送形式：定时发送</li><li>发送原因：通过用例定时监控维护接口</li><li>基础数据：<a href="+"http://10.9.2.142:18080/HttpInterfaceTest/report/SKU.action"+
	">基础数据平台&接口测试报告</a></li><li>OFC项目： <a href="+
			"http://10.9.2.142:18080/HttpInterfaceTest/report/OFC.action"+
	">OFC接口测试报告</a></li><li>ODC项目：<a href=" + 
			"http://10.9.2.142:18080/HttpInterfaceTest/report/ODC.action"+">ODC接口测试报告</a></li>";
	
	 public static boolean send(String to,String copyto, String subject,  String smtp, String host,
	            String sendName, String sendPort, String userName, String userPwd) {
	 
	        // 第一步：创建Session
	        Properties props = new Properties();
	        // 指定邮件的传输协议，smtp(Simple Mail Transfer Protocol 简单的邮件传输协议)
	        props.put("mail.transport.protocol", smtp);
	        // 指定邮件发送服务器服务器 "smtp.qq.com"
	        props.put("mail.host", host);
	        // 指定邮件的发送人(您用来发送邮件的服务器，比如您的163\sina等邮箱)
	        props.put("mail.from", sendName);
	        if (true) {
	            props.put("mail.smtp.starttls.enable", "true");
	            props.put("mail.smtp.socketFactory.fallback", "false");
	            props.put("mail.smtp.socketFactory.port", sendPort);
	        }
	        Session session = Session.getDefaultInstance(props);
	 
	        // 开启调试模式
	        session.setDebug(true);
	        try {
	            // 第二步：获取邮件发送对象
	            Transport transport = session.getTransport();
	            // 连接邮件服务器，链接您的163、sina邮箱，用户名（不带@163.com，登录邮箱的邮箱账号，不是邮箱地址）、密码
	            transport.connect(userName, userPwd);
	            //Address toAddress = new  InternetAddress(to);
	 
	            // 第三步：创建邮件消息体
	            MimeMessage message = new MimeMessage(session);
	            //设置自定义发件人昵称
	           /* String nick="";
	            try {
	                nick=javax.mail.internet.MimeUtility.encodeText("我的昵称");
	            } catch (UnsupportedEncodingException e) {
	                e.printStackTrace();
	            }
	            message.setFrom(new InternetAddress(nick+" <"+sendName+">"));*/
	          //设置发信人
	           message.setFrom(new InternetAddress(sendName));
	 
	            // 邮件的主题
	            //message.setSubject(subject);
	            
	            
	            //使用Bese64编码邮件主题,防止乱码
	            @SuppressWarnings("restriction")
				sun.misc.BASE64Encoder base64 = new BASE64Encoder();
	            String subjectNew = new String(base64.encode(subject.getBytes("UTF-8")));
	            //mailMessage.setSubject("=?UTF-8?B?" + subject + "?=");
	            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
	         //   mailMessage.setSubject(mailInfo.getSubject())
	            
	            
	            //收件人
	            //message.addRecipient(Message.RecipientType.TO, toAddress)
	            message.setRecipient(Message.RecipientType.TO, new InternetAddress(
	                    to));;
	            //抄送人
                if (copyto != null && copyto.length() > 0) {
                   
                    message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copyto)); 
                }
             // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
                Multipart mainPart = new MimeMultipart();
                // 创建一个包含HTML内容的MimeBodyPart
                BodyPart html = new MimeBodyPart();
                // 设置HTML内容
	            html.setContent(content, "text/html;charset=UTF-8");	            
                mainPart.addBodyPart(html);
                // 将MiniMultipart对象设置为邮件内容
                message.setContent(mainPart);
                // 发送邮件

                
	            
	            // 邮件发送时间
	            message.setSentDate(new Date());
	 
	            // 第四步：发送邮件
	            // 第一个参数：邮件的消息体
	            // 第二个参数：邮件的接收人，多个接收人用逗号隔开（test1@163.com,test2@sina.com）
	            transport.sendMessage(message, message.getAllRecipients());
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	 
		 
	 	public static String getContent(File file){
	        StringBuilder result = new StringBuilder();
	        try{
	            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result.append(System.lineSeparator()+s);
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result.toString();
	    }

}
