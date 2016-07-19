package com.sky.nighthawk.model.protocol.HttpXML.codec;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderUtil.setContentLength;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by xpf on 2016/07/18.
 */
public class HttpXmlRequestEncoder  extends
        AbstractHttpXmlEncoder<HttpXmlRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlRequest msg,
                          List<Object> out) throws Exception {
        ByteBuf body = encode0(ctx, msg.getBody());
        FullHttpRequest request = msg.getRequest();
        if (request == null) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.GET, "/do", body);
            HttpHeaders headers = request.headers();
            headers.set(HttpHeaderNames.HOST, InetAddress.getLocalHost()
                    .getHostAddress());
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            headers.set(HttpHeaderNames.ACCEPT_ENCODING,
                    HttpHeaderValues.GZIP.toString() + ','
                            + HttpHeaderValues.DEFLATE.toString());
            headers.set(HttpHeaderNames.ACCEPT_CHARSET,
                    "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            headers.set(HttpHeaderNames.ACCEPT_LANGUAGE, "zh");
            headers.set(HttpHeaderNames.USER_AGENT,
                    "Netty xml Http Client side");
            headers.set(HttpHeaderNames.ACCEPT,
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        }
        setContentLength(request, body.readableBytes());
        out.add(request);
    }

}

