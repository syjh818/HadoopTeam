package com.cxy;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

@AllArgsConstructor
@NoArgsConstructor
public class PredictImpl implements Predict{
    private PredictProperties predictProperties;
    //记录每一个类标_词语及其计数
    private static final HashMap<String, Integer> labelAndFeatureMap = new HashMap<>();
    //记录每一个类标及其计数
    private static final HashMap<String, Integer> labelMap = new HashMap<>();
    //记录所有的类标
    private static final HashSet<String> labelSet = new HashSet<>();
    //训练数据集总数
    private static Long trainingCount = 0L;

    public static void initModel(String path) throws IOException {
        //初始化模型
        BufferedReader bufferedReader =
                new BufferedReader(new FileReader(path));
        //逐行读入模型
        String line = bufferedReader.readLine();
        String key;
        int count;
        long trainingCount1 = 0L;
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
    }

    public PredictVO predict(String line){
        String expectation = line.contains(":") ? line.split(":")[0] : line.split("：")[0];
        String word = line.contains(":") ? line.split(":")[1] : line.split("：")[1];
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

    /*public static void main(String[] args) throws IOException {
        int trueCount = 0;
        int falseCount = 0;
        String modelPath = "D:\\hadoop\\modeloutput\\part-r-00000";
        initModel(modelPath);
        String testPath = "D:\\hadoop\\test.txt";
        BufferedReader bufferedReader =
                new BufferedReader(new FileReader(testPath));
        //逐行读入模型
        String line = bufferedReader.readLine();
        while(line != null){
            String trueLabel = line.contains(":") ? line.split(":")[0] : line.split("：")[0];
            System.out.print(line);
            String predictLabel = predict(line);
            System.out.println("\t预测结果：" + predictLabel);
            if(trueLabel.equals(predictLabel)){
                trueCount++;
            }else{
                falseCount++;
            }
            line = bufferedReader.readLine();
        }
        //System.out.println("预测正确：" + trueCount);
        //System.out.println("预测错误：" + falseCount);
        System.out.println("正确率：" + (double)trueCount / (trueCount + falseCount));
    }*/
}
