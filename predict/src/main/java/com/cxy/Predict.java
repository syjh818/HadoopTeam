package com.cxy;

public interface Predict {

    /**
     * 生成预测结果
     * @param line 待预测文件的每一行
     * @return 返回PredictVO包装类
     */
    PredictVO predict(String line);
}
