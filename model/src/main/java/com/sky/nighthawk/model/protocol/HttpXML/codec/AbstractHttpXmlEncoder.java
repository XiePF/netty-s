package com.sky.nighthawk.model.protocol.HttpXML.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by xpf on 2016/07/18.
 */
public class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {
    IBindingFactory factory = null;
    StringWriter writer = null;
    final static String CHARSET_NAME = "UTF-8";
    final static Charset UTF_8 = Charset.forName(CHARSET_NAME);


    protected ByteBuf encodeo(ChannelHandlerContext ctx, Object body)
            throws Exception {
        factory = BindingDirectory.getFactory(body.getClass());
        writer = new StringWriter();
        IMarshallingContext mctx = factory.createMarshallingContext();
        mctx.setIndent(2);
        mctx.marshalDocument(body, CHARSET_NAME, null, writer);
        String xmlStr = writer.toString();
        writer.close();
        writer = null;
        ByteBuf encodeBuf = Unpooled.copiedBuffer(xmlStr, UTF_8);
        return encodeBuf;
    }

    /**
     * Calls {@link ChannelHandlerContext#fireExceptionCaught(Throwable)} to
     * forward to the next {@link ChannelHandler} in the {@link ChannelPipeline}
     * .
     * <p>
     * Sub-classes may override this method to change behavior.
     */
    @Skip
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // 释放资源
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, T t, List<Object> list) throws Exception {

    }
}
