//package com.pmpsdk.unuse;
//
//import cn.hutool.json.JSONUtil;
//
//import com.pmpsdk.domain.AlertRule;
//import com.pmpsdk.utils.LogUtil;
//import com.pmpsdk.utils.PostToServer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.pmpsdk.utils.PostUrl.GET_ALERT_RULE;
//import static com.pmpsdk.utils.SpringContextUtil.PROJECT_TOKEN;
//
//public class AlertUtil {
//    @Value("${alert.webhook-url}")
//    private String webhookUrl;
//
//    /**
//     * 获取该项目下，异常类型及其阈值
//     * @return
//     */
//    public static HashMap<String, Integer> getAlertRuleMap() {
//        try {
//            // 获取所有异常类型及其阈值
//            List<AlertRule> rules = JSONUtil.toList(
//                    PostToServer.sendJson(GET_ALERT_RULE.getUrl(), PROJECT_TOKEN),
//                    AlertRule.class
//            );
//
//            // 定义返回结果
//            HashMap<String, Integer> ruleMap = new HashMap<>();
//            for (AlertRule rule : rules) {
//                ruleMap.put(rule.getErrorType(), rule.getThreshold());
//            }
//            return ruleMap;
//        } catch (Exception e) {
//            LogUtil.error("获取异常类型及其阈值失败: " + e.getMessage());
//            return new HashMap<>();
//        }
//    }
//
//    /**
//     * 发送报警信息
//     *
//     * @param message
//     */
//    public void sendAlert(String message) {
//        // 实现企业微信告警
//        // 这里简单示例，实际可调用HTTP接口发送告警
//        System.out.println("Sending alert: " + message);
//
//        // 示例：调用企业微信机器人
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            Map<String, Object> body = new HashMap<>();
//            body.put("msgtype", "text");
//            body.put("text", Map.of("content", message));
//
//            RestTemplate restTemplate = new RestTemplate();
//            restTemplate.postForEntity(webhookUrl,
//                    new HttpEntity<>(body, headers), String.class);
//        } catch (Exception e) {
//            LogUtil.error("发送告警失败: " + e.getMessage());
//        }
//    }
//
//    /**
//     * 告警升级
//     * @param module
//     * @param exceptionType
//     * @param count
//     */
//    public static void upgradeAlert(String module, String exceptionType, int count) {
//        String message = String.format(
//                "【告警升级】模块: %s, 异常类型: %s, 当前次数: %d\n" +
//                        "该异常已持续超过3倍阈值且未解决，请立即处理！",
//                module, exceptionType, count);
//
//        // 发送给更高级别的负责人
//    }
//}
