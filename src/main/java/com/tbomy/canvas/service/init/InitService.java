package com.tbomy.canvas.service.init;

import com.tbomy.canvas.param.response.Circle;
import com.tbomy.canvas.param.response.CircleTrack;
import com.tbomy.canvas.param.response.InitLine;

import java.util.ArrayList;

public interface InitService {
    Object[] initTracks();
    
    Object[] initCircleArr(String name);
    
    void computeDiffAngleOnSameTrack();
    
    ArrayList<String> getColorList();
    
    Double[] getDiffAngleOnSameTrack();
    
    CircleTrack[] getCircleTrackArr();
    
    InitLine getInitLine();
}
