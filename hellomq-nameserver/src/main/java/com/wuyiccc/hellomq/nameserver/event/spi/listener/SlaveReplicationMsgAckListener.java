package com.wuyiccc.hellomq.nameserver.event.spi.listener;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.dto.SlaveAckDTO;
import com.wuyiccc.hellomq.common.enums.NameServerResponseCodeEnum;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.event.model.SlaveReplicationMsgAckEvent;

import java.nio.charset.StandardCharsets;

/**
 * @author wuyiccc
 * @date 2024/10/20 09:39
 * <p>
 * 主节点接收从节点同步信号处理器
 */
public class SlaveReplicationMsgAckListener implements Listener<SlaveReplicationMsgAckEvent> {
    @Override
    public void onReceive(SlaveReplicationMsgAckEvent event) throws Exception {

        String slaveAckMsgId = event.getMsgId();
        SlaveAckDTO slaveAckDTO = CommonCache.getAckMap().get(slaveAckMsgId);

        if (slaveAckDTO == null) {
            return;
        }

        int ackTime = slaveAckDTO.getNeedAckTime().decrementAndGet();

        // 如果用 <= 0 判断, 那么半同步复制这种并发情况下, <= 0的情况可能会出现多次, 导致后面的步骤重复执行
        if (ackTime == 0) {
            CommonCache.getAckMap().remove(slaveAckMsgId);
            slaveAckDTO.getBrokerChannel().writeAndFlush(new TcpMsg(NameServerResponseCodeEnum.REGISTRY_SUCCESS.getCode()
                    , NameServerResponseCodeEnum.REGISTRY_SUCCESS.getDesc().getBytes(StandardCharsets.UTF_8)));
        }
    }
}
