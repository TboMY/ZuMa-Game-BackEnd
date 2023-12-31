package com.tbomy.canvas.service.clear.impl;

import com.tbomy.canvas.param.request.Clear;
import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.service.clear.ClearService;
import com.tbomy.canvas.util.Util;
import org.springframework.stereotype.Service;

/*@author cj
 *@date 2023/12/31 15:56:18
 */
@Service
public class ClearServiceImpl implements ClearService {
    @Override
    public Circle[] clear(Clear clear) {
        Circle[] arr = clear.getCircleArr();
        int index = clear.getIndex();
        return Util.tryRemoveSameColorCircle(index, arr);
    }
}
