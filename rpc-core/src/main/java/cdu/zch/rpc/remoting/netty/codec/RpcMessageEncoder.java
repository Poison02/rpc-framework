package cdu.zch.rpc.remoting.netty.codec;

import cdu.zch.rpc.compress.Compress;
import cdu.zch.rpc.enums.CompressTypeEnum;
import cdu.zch.rpc.enums.SerializationTypeEnum;
import cdu.zch.rpc.extension.ExtensionLoader;
import cdu.zch.rpc.remoting.constants.RpcConstants;
import cdu.zch.rpc.remoting.dto.RpcMessage;
import cdu.zch.rpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义协议编码器
 * <pre>
 * 0     1     2     3     4        5     6     7     8         9          10      11     12  13  14   15 16
 * +-----+-----+-----+-----+--------+----+----+----+------+-----------+-------+----- --+-----+-----+-------+
 * |   magic   code        |version | full length         | messageType| codec|compress|    RequestId      |
 * +-----------------------+--------+---------------------+-----------+-----------+-----------+------------+
 * |                                                                                                       |
 * |                                         body                                                          |
 * |                                                                                                       |
 * |                                        ... ...                                                        |
 * +-------------------------------------------------------------------------------------------------------+
 * 4B  magic code（魔法数）   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
 * 1B compress（压缩类型） 1B codec（序列化类型）    4B  requestId（请求的Id）
 * body（object类型数据）
 * </pre>
 * @author Zch
 * @date 2023/7/21
 **/
@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    /**
     * 这里使用AtomicInteger作为请求ID，每次自增即可
     */
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage rpcMessage, ByteBuf out){
        try {
            // 魔法数长度
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
            // 版本号长度
            out.writeByte(RpcConstants.VERSION);
            // 预留4个字节，写入数据长度
            out.writerIndex(out.writerIndex() + 4);
            byte messageType = rpcMessage.getMessageType();
            // 消息类型长度
            out.writeByte(messageType);
            // 序列化类型长度
            out.writeByte(rpcMessage.getCodec());
            // 压缩类型长度
            out.writeByte(CompressTypeEnum.GZIP.getCode());
            // 请求ID
            out.writeInt(ATOMIC_INTEGER.getAndIncrement());
            byte[] bodyBytes = null;
            // 这里的fullLength就是总长度，头部长度为16
            int fullLength = RpcConstants.HEAD_LENGTH;
            // if messageType is not heartbeat message,fullLength = head length + body length 不是心跳的话，就是头部长度加上body长度
            if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE
                && messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                // 序列化类型
                String codecName = SerializationTypeEnum.getName(rpcMessage.getCodec());
                log.info("codec name : [{}]", codecName);
                Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(codecName);
                // 对消息体进行序列化
                bodyBytes = serializer.serialize(rpcMessage.getData());
                // 对消息体进行压缩
                String compressName = CompressTypeEnum.getName(rpcMessage.getCompress());
                Compress compress = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(compressName);
                bodyBytes = compress.compress(bodyBytes);
                // 最后的fullLength就是头部加上消息体长度
                fullLength += bodyBytes.length;
            }
            if (bodyBytes != null) {
                // 写入消息体
                out.writeBytes(bodyBytes);
            }
            // 如果是心跳
            // 获取当前写索引位置
            int writeIndex = out.writerIndex();
            // 移动写索引
            out.writerIndex(writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
            out.writeInt(fullLength);
            out.writerIndex(writeIndex);
        } catch (Exception e) {
            log.error("Encode request error!", e);
        }
    }
}
