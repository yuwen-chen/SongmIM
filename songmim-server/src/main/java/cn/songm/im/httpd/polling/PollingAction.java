package cn.songm.im.httpd.polling;

import org.springframework.beans.factory.annotation.Autowired;

import cn.songm.im.IMException.ErrorCode;
import cn.songm.im.httpd.HttpAction;
import cn.songm.im.model.SessionCh;
import cn.songm.im.service.SessionService;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public abstract class PollingAction extends HttpAction {

    @Autowired
    protected SessionService sessionService;

    public void checkSession(HttpRequest req) throws PollingException {
        HttpHeaders headers = req.headers();
        String sesId = headers.getAndConvert("session");
        SessionCh ses = sessionService.getSession(sesId);
        if (ses == null) {
            QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
            throw new PollingException(ErrorCode.SESSION_DISABLED, "Session失败",
                    this.getParamValue(decoder, "callback"));
        }
    }
}
