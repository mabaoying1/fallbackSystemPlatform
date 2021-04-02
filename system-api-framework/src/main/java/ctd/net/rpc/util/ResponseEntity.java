package ctd.net.rpc.util;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @ClassName ResponseEntity
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/19
 * @Version V1.0
 **/
@Data
public class ResponseEntity implements Serializable {

    private int Code;

    private String Msg;

    private Object Result;

    public ResponseEntity(int code, String msg, Object result){
        this.Code = code;
        this.Msg = msg;
        this.Result = result;
    }
}
