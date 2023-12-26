package com.cxy;

import java.io.File;
import java.util.List;

public interface Predict {

    /**
     * 生成输入一组词语的预测结果
     * @param word 待预测的词语
     * @return 返回PredictVO包装类
     */
    PredictVO predict(String word);

    /**
     * 生成输入文件的预测结果
     * @param file 待预测的文件
     * @return 返回PredictVO的List
     */
    List<PredictVO> predict(File file);
}
