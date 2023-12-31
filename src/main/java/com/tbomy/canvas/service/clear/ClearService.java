package com.tbomy.canvas.service.clear;

import com.tbomy.canvas.param.request.Clear;
import com.tbomy.canvas.param.response.Circle;

public interface ClearService {
    Circle[] clear(Clear reHit);
}
