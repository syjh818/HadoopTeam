package com.cxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictVO {
    /**
     * 预测词 以空格分隔
     */
    private String word;
    /**
     * 预期结果
     */
    private String expectation;
    /**
     * 实际结果
     */
    private String actuality;
}
