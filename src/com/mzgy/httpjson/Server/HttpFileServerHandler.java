package com.mzgy.httpjson.Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import static com.sun.deploy.net.HttpRequest.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by mypc on 2017/8/26.
 */
public class HttpFileServerHandler extends ChannelInboundHandlerAdapter {
    private HttpRequest request;
    private FullHttpResponse response;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            request = (HttpRequest) msg;
            String url = request.uri();
            HttpMethod method = request.method();
            HttpVersion version = request.protocolVersion();
            }
            if(response!=null)
                ctx.write(response);

        if (msg instanceof HttpContent){
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();

            String res = "I am OK";
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(res).append("</br>").append("<table><tr><th>标题</th><th>作者</th></tr><tr><td>1</td><td>2</td></tr></table>");
            response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(CONTENT_TYPE, "text/html;charset=UTF-8");
            ByteBuf byteBuf = Unpooled.copiedBuffer(stringBuffer, CharsetUtil.UTF_8);
            response.content().writeBytes(byteBuf);
            byteBuf.release();
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            ctx.flush();
        }
    }
}
