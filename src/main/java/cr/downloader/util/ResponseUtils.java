package cr.downloader.util;

import cr.downloader.web.vo.ResponseVO;

/**
 * @author Beldon
 * @create 2018-08-14 17:23
 */
public class ResponseUtils {

    public static ResponseVO success() {
        return response(0, "success", null);
    }

    public static ResponseVO success(String msg) {
        return response(0, msg, null);
    }

    public static <T> ResponseVO successWithData(T data) {
        return response(0, "", data);
    }

    public static <T> ResponseVO error(int code, String msg) {
        return response(code, msg, "");
    }

    public static <T> ResponseVO response(int code, String msg, T data) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setCode(code);
        responseVO.setMsg(msg);
        responseVO.setData(data);
        return responseVO;
    }
}
