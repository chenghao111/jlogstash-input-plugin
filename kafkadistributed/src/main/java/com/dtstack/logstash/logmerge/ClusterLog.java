package com.dtstack.logstash.logmerge;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 日志信息,包含日志发送时间
 * Date: 2016/12/28
 * Company: www.dtstack.com
 *
 * @ahthor xuchao
 */

public class ClusterLog {

    private static final Logger logger = LoggerFactory.getLogger(ClusterLog.class);

    private static Gson gson = new Gson();

    private long logTime;

    private String host;

    private String path;

    private String loginfo;

    private String originalLog;

    private String logType;

    public long getLogTime() {
        return logTime;
    }

    public void setLogTime(long logTime) {
        this.logTime = logTime;
    }

    public String getLoginfo() {
        return loginfo;
    }

    public void setLoginfo(String loginfo) {
        this.loginfo = loginfo;
    }

    @Override
    public String toString() {
        return loginfo;
    }

    public String getLogFlag(){
        return host + ":" + path;
    }

    public String getOriginalLog() {
        return originalLog;
    }

    public void setOriginalLog(String originalLog) {
        this.originalLog = originalLog;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    /**
     * 获取除了message字段以外的信息
     * @return
     */
    public Map<String, Object> getBaseInfo(){
        Map<String, Object> eventMap = null;
        try{
            eventMap = gson.fromJson(originalLog, Map.class);
        }catch (Exception e){
            logger.error("解析 log json 对象异常", e);
            return null;
        }

        eventMap.remove("message");
        return eventMap;
    }

    public static ClusterLog generateClusterLog(String log) {
        Map<String, String> eventMap = null;
        try{
           eventMap = gson.fromJson(log, Map.class);
        }catch (Exception e){
            logger.error("解析 log json 对象异常", e);
            return null;
        }

        ClusterLog clusterLog = new ClusterLog();
        long time = Long.valueOf(eventMap.get("timestamp"));
        String msg = eventMap.get("message");
        String host = eventMap.get("host");
        String path = eventMap.get("path");
        String logType = eventMap.get("logtype");

        clusterLog.setLogTime(time);
        clusterLog.setLoginfo(msg);
        clusterLog.originalLog = log;
        clusterLog.host = host;
        clusterLog.path = path;
        clusterLog.logType = logType;

        return clusterLog;
    }
}
