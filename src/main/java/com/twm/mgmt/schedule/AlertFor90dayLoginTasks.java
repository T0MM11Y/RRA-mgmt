package com.twm.mgmt.schedule;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.twm.mgmt.Enum.LogFormat;
import com.twm.mgmt.controller.BaseController;
import com.twm.mgmt.controller.RedirectController;
import com.twm.mgmt.model.common.MailVo;
import com.twm.mgmt.service.AlertFor90dayLoginService;
import com.twm.mgmt.utils.AESUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Configurable
@EnableScheduling
public class AlertFor90dayLoginTasks extends BaseController {
    @Value("${environment.name}")
    private String envName;
    @Value("${webApp.url}")
    private String webApp_url;
    @Value("${rra.mail.from}")
    private String rra_mail_from;
    @Autowired
    private AlertFor90dayLoginService alertFor90dayLoginService;

    @Scheduled(cron = "${cron.alertFor90dayLogin}")
    public void alertFor90dayLogin() throws UnknownHostException {
        String schedule_id = "alertFor90dayLogin_RRA WEB 用戶登入時間提醒排程" + dateFormat().format(new Date());
        InetAddress addr = InetAddress.getLocalHost();
        int ipTail = Integer.parseInt(StringUtils.split(addr.getHostAddress(), ".")[3]);
        if (envName.equals("DEV") || envName.equals("UAT"))
            return;
        if (envName.equals("PROD") && ipTail % 2 == 0)
            return;
        try {
            log.info(commonFormat, "-", schedule_id, "-", "只有在00:30，才會啟動排程", "-", "-", "-", "-");
            log.info(commonFormat, "-", schedule_id, "-", "addr:", addr, "PROD環境只有ip尾數是奇數的server才會執行此排程", "-", "-");
            alertFor90dayLoginService.alertFor90dayLogin();
            Map<String, Object> map = new HashMap<>();
            MailVo mailVo;
            Set<String> almostDisableSet = alertFor90dayLoginService.getAlmostDisableSet();
            if (!almostDisableSet.isEmpty()) {
                log.info("70天, 75天, 80天, 85天未登入要寄RRA WEB提醒登入通知信,toEmails:{}", almostDisableSet);
                map.put("name", "RRA WEB提醒登入通知");
                map.put("message1", "因您久未登入RRA WEB，特發此信作為提醒登入通知。如超過90天仍未登入，RRA WEB會將您的帳號設定為停用。需要提單申請才能再次啟用帳號。");
                map.put("message2", "建議您可登入RRA WEB : ");
                map.put("message3", webApp_url);
                map.put("CustomerRewardSystem", webApp_url);
                mailVo = MailVo.builder().subject("RRA WEB提醒登入通知").from(rra_mail_from).toEmails(almostDisableSet)
                        .build();
                mailVo.setParams(map);
                mailUtils.sendMail(mailVo, "rraWebAccountTemplate.html");
            } else {
                log.info("沒有要寄RRA WEB提醒登入通知信");
            }
            Set<String> disabledSet = alertFor90dayLoginService.getDisabledSet();
            if (!disabledSet.isEmpty()) {
                log.info("要寄RRA WEB停用您的帳號通知信,toEmails:{}", disabledSet);
                map = new HashMap<>();
                map.put("name", "RRA WEB停用您的帳號通知");
                map.put("message1", "因您超過90天未登入RRA WEB，特發此信作為停用您的RRA WEB帳號的通知。如您之後需要使用RRA WEB，需要提單申請才能再次啟用帳號。");
                map.put("message2", "");
                map.put("message3", "");
                map.put("CustomerRewardSystem", "");
                mailVo = MailVo.builder().subject("RRA WEB停用您的帳號通知").from(rra_mail_from).toEmails(disabledSet).build();
                mailVo.setParams(map);
                mailUtils.sendMail(mailVo, "rraWebAccountTemplate.html");
            } else {
                log.info("沒有要寄RRA WEB停用您的帳號通知信");
            }
        } catch (Exception e) {
            log.info(commonFormat, "-", schedule_id, "-", "-", "失敗", "-", "-", e.toString());
            log.error(errorFormat, "-", schedule_id, "-", "失敗", e.toString(), e.toString());
        } finally {
        }
    }
}