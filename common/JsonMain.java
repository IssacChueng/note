package common;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author swzhang
 * @date 2019/3/12
 * @description
 */
public class JsonMain {
    
    public static void main(String[] args) {
        String json = "{\"relationType\":1,\"replyType\":\"5\",\"hospCode\":\"joinhealth166\",\"answers\":[{\"questionId\":\"07909269d6ec4097880d2636fc472fe5\",\"questionAnswer\":[\"e1835f4def0e4deea6d397cd09cf5006\",\"8a4d4640bbc14f0fa72ab2819f77838d\"],\"quoteQuestionId\":\"\",\"quoteQuestionAnswer\":\"\",\"questionType\":3},{\"questionId\":\"375f803868fd4df090ba72d29ce3211f\",\"questionAnswer\":\"7bdc4620f46e433f9d940e09db75e731\",\"quoteQuestionId\":\"\",\"except\":false,\"quoteQuestionAnswer\":\"\",\"questionType\":2},{\"questionId\":\"700c04018d6642cc962d122ce6acac82\",\"questionAnswer\":\"意图一体化健康\",\"quoteQuestionId\":\"\",\"quoteQuestionAnswer\":\"\",\"questionType\":1},{\"questionId\":\"c0ef295da5bb406b8c2ca824df1042ce\",\"questionAnswer\":[\"f58d16cf819b4b48bd1c02910bd7e18e\"],\"quoteQuestionId\":\"\",\"quoteQuestionAnswer\":\"\",\"questionType\":3}],\"relationId\":\"cb1ec9a7410a4c818092ec839165292b\",\"questionnaireId\":\"d42d1d9190e24cdd94fd9276b57a4943\"}";

    }

    private String generateAnswer(String examineResultJson) {
        JSONObject formAnswer = JSON.parseObject(examineResultJson);
        if (formAnswer == null) {
            return null;
        }
        JSONArray answers = formAnswer.getJSONArray("answers");
        if (answers != null && !answers.isEmpty()) {
            List<Integer> indexRemove = new ArrayList<>(answers.size());
            int size = answers.size();
            for (int i = 0; i < size; i++) {
                JSONObject answer = answers.getJSONObject(i);
                if (answer == null) {
                    continue;
                }
                Integer questionType = answer.getInteger("questionType");
                if (3 == questionType) {
                    indexRemove.add(i);
                    JSONArray questionAnswer = answer.getJSONArray("questionAnswer");
                    if (questionAnswer != null && !questionAnswer.isEmpty()) {
                        List<String> strings = questionAnswer.toJavaList(String.class);
                        for (int j = 0; j < questionAnswer.size(); j++) {
                            JSONObject clone = (JSONObject) answer.clone();
                            clone.put("questionAnswer", strings.get(j));
                            answers.add(clone);
                        }
                    }
                }
            }

            //循环结束后将所有之前answer为list的多选答案删除
            indexRemove.forEach(index -> answers.remove(index.intValue()));
        }
        return formAnswer.toJSONString();
    }
}
