//package demo.im.system.mq;
//
//import com.alibaba.fastjson.JSON;
//import demo.im.system.send.AppMsgSend;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
//
//@Component
//public class IMConsumer extends CamelConsumer {
//
//    @Autowired
//    private IMDao imDao;
//
//    @Resource(name = "shardedRedisProxyService")
//    private IRedisProxy iRedisProxy;
//
//    public static final String REDIS_KEY_PREFIX_USER_MSG = "msg.user:";
//
//    @Override
//    public void receive() throws Exception {
//        from(
//                "kafka:{{metadata.broker.list}}?topic="
//                        + MesMsgTopic.IM_TOPIC.getCode()
//                        + "&groupId={{group.id}}")
//                .process(new MestarMsgProcessor() {
//
//                    @Transactional
//                    @Override
//                    public void process(String message) throws Exception {
//                        MestarLogger.info("IMConsumer接收消息："
//                                + message + ",当前时间："
//                                + System.currentTimeMillis());
//                        try {
//                            MsgBO msgBO = (MsgBO) JSONUtil.readJSON2Bean(message, MsgBO.class);
//                            String msgConentId = msgBO.getMsgContentId();
//                            String msgRelationId = msgBO.getMsgRelationId();
//                            AppMsgSend appMsgSend = new AppMsgSend(iRedisProxy);
//                            if (StringUtils.isNotBlank(msgRelationId)) {//APP用户上线重连，将未发送的消息推给app用户
//                                //避免重复消费
//                                IMMsgRelation relation = imDao.get(IMMsgRelation.class, msgRelationId);
//                                if (relation == null) {
//                                    return;
//                                }
//                                if (relation.getIsSend() == IMMsgRelation.IS_SEND_YES) {
//                                    MestarLogger.info("IMConsumer消息已处理过，msgRelationId：" + msgRelationId + ",当前时间：" + System.currentTimeMillis());
//                                    return;
//                                }
//                                String user = relation.getRecipientId();
////                            IMMsgContent msgContent = relation.getMsg();
//                                IMMsgContent msgContent = imDao.get(IMMsgContent.class, relation.getMsgId());
//
//                                if (appMsgSend.isOnline(user)) {
//                                    // 发送app通知
//                                    appMsgSend.send(JSON.toJSONString(msgContent), user);
//                                    relation.setIsSend(IMMsgRelation.IS_SEND_YES);
//                                    imDao.save(relation);
//
//                                /*// 发送用户未读数信息
//                                Map<String, String> noReadMap = iRedisProxy.hgetAll(REDIS_KEY_PREFIX_USER_MSG+user);
//                                String noReadMsg = JSON.toJSONString(noReadMap);
//                                appMsgSend.send(noReadMsg, user);*/
//                                }
//                            } else if (StringUtils.isNotBlank(msgConentId)) {//走消息推送正常流程
//                                //避免重复消费
//                                IMMsgContent msgContent = imDao.get(IMMsgContent.class, msgConentId);
//                                if (msgConentId == null) {
//                                    return;
//                                }
//                                if (msgContent.getIsSend() == IMMsgContent.IS_SEND_YES) {
//                                    MestarLogger.info("IMConsumer消息已处理过，msgConentId：" + msgConentId + ",当前时间：" + System.currentTimeMillis());
//                                    return;
//                                }
//
//                                //发送链，集成其他消息渠道
//                                SendChain sendChain = new SendChain();
//                                sendChain.addSend(appMsgSend);//添加app消息发送渠道
//
//                                msgContent.setIsSend(IMMsgContent.IS_SEND_YES);
//                                imDao.save(msgContent);
//
//                                //发送消息
//                                List<IMMsgRelation> relations = imDao.find(IMMsgRelation.class, new String[] {"msgId", "isDelete", "isSend"}
//                                        , new Object[] {msgConentId, 0, 0});
//                                if (relations != null && relations.size() > 0) {
//                                    relations.stream().forEach(relation -> {
//                                        String user = relation.getRecipientId();
//                                        if (appMsgSend.isOnline(user)) {
//                                            sendChain.send(JSON.toJSONString(msgContent), user);
//                                            relation.setIsSend(IMMsgRelation.IS_SEND_YES);
//                                            imDao.save(relation);
//
//                                        /*//发送未读数
//                                        Map<String, String> noReadMap = iRedisProxy.hgetAll(REDIS_KEY_PREFIX_USER_MSG+user);
//                                        String noReadMsg = JSON.toJSONString(noReadMap);
//                                        appMsgSend.send(noReadMsg, user);*/
//                                        }
//                                    });
//                                }
//                            }
//                        } catch (Exception e) {
//                            MestarLogger.error(e.getMessage(), e);
//                        } finally {
//                            MestarLogger.info("IMConsumer结束处理消息：" + message + ",当前时间：" + System.currentTimeMillis());
//                        }
//                    }
//                }).to("log:input").end();
//    }
//}
