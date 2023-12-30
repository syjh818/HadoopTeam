package com.cxy;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Log4j2
@NoArgsConstructor
public class BayesPredictImpl implements Predict{
    private PredictProperties predictProperties;
    //记录每一个类标_词语及其计数
    private static final HashMap<String, Integer> labelAndFeatureMap = new HashMap<>();
    //记录每一个类标及其计数
    private static final HashMap<String, Integer> labelMap = new HashMap<>();
    //记录所有的类标
    private static final HashSet<String> labelSet = new HashSet<>();
    //训练数据集总数
    private static Long trainingCount = 0L;

    public static void initModel(String path){
        try {
            //初始化模型
            BufferedReader bufferedReader =
                    new BufferedReader(new FileReader(path));
            //逐行读入模型
            String line = bufferedReader.readLine();
            String key;
            int count;
            while(line != null){
                key = line.split("\\t")[0];
                count = Integer.parseInt(line.split("\\t")[1]);
                if(line.contains("_")){//该行是类标_词语
                    labelAndFeatureMap.put(key, count);
                }else{//该行是类标
                    labelMap.put(key, count);
                    labelSet.add(key);
                    trainingCount += count;
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            log.error("读取模型文件错误");
            throw new RuntimeException(e);
        }
    }


    public BayesPredictImpl(PredictProperties predictProperties) {
        this.predictProperties = predictProperties;
    }

    /**
     * 使用贝叶斯预测
     * @param expectation 预期结果，如传入时为NULL，返回包装类中expectation也为NULL
     * @param word 需要预测的词组
     * @return 返回包装类PredictVO
     */
    public PredictVO predict(String expectation, String word){
        //检查模型是否初始化
        if(trainingCount == 0L){
            initModel(predictProperties.getModelPath());
        }
        double priori;
        double likelihood;
        double predictProbability = 0L;
        String predictLabel = "";
        //计算属于各个类别的概率
        for (String label : labelSet) {
            //计算先验概率
            priori = labelMap.get(label) /  (double)trainingCount;
            likelihood = 1L;
            boolean flag = false;
            //获取所有特征，使用多项式原理计算似然概率
            for (String feature : word.split(" ")) {
                if(!(labelAndFeatureMap.get(label + "_" + feature) == null)){//训练数据集有该组合
                    likelihood *=  labelAndFeatureMap.get(label + "_" + feature)
                            / (double)labelMap.get(label);
                    flag = true;
                }
            }
            //flag为false意味着训练集中没有任何类标_词语与之匹配，跳过比较
            if(flag && predictProbability < likelihood * priori){//替换预测结果为此次循环的label
                predictProbability = likelihood * priori;
                predictLabel = label;
            }
        }
        return new PredictVO(word, expectation, predictLabel);
    }

    @Override
    public PredictVO predict(String word){
        return predict(null, word);

    }

    @Override
    public List<PredictVO> predict(InputStream file) {
        BufferedWriter writer;
        BufferedReader reader;
        List<PredictVO> predictVOList = new LinkedList<>();
        try {
            writer = new BufferedWriter(new FileWriter(predictProperties.getOutputPath()));
            reader = new BufferedReader(new InputStreamReader(file));
            String line;
            int i = 1;//记录行号
            while ((line = reader.readLine()) != null) {
                String expectation = line.contains(":") ? line.split(":")[0] : line.split("：")[0];
                String word = line.contains(":") ? line.split(":")[1] : line.split("：")[1];
                PredictVO predictVO = predict(expectation, word);
                writer.write(i++ + "\t" + predictVO.getExpectation());
                predictVOList.add(predictVO);
            }
        } catch (Exception e) {
            log.error("输出预测结果文件错误");
            throw new RuntimeException(e);
        }
        return predictVOList;
    }
}
