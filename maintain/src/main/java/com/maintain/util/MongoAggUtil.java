package com.maintain.util;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class MongoAggUtil {

    @Resource
    private MongoTemplate mongoTemplate;

    public Long sum(Criteria query, String groupField, String sumField, String collName){
        MatchOperation operationQuery = null;
        if(query != null) {
            operationQuery = Aggregation.match(query);
        }
        // 聚合条件
        GroupOperation operation = Aggregation.group(groupField).sum(sumField).as("sum");
        // 创建聚合对象
        Aggregation aggregation = null;
        if(operationQuery != null) {
            aggregation = Aggregation.newAggregation(operationQuery,
                operation);
        }else {
            aggregation = Aggregation.newAggregation(operation);
        }
        // 执行，返回
        AggregationResults<LinkedHashMap> aResult = mongoTemplate.aggregate(
                aggregation, collName, LinkedHashMap.class);
        Document rResult = aResult.getRawResults();
        List list = (List) rResult.get("results");
        if(!CollectionUtils.isEmpty(list)) {
            Document res = (Document)list.get(0);
            return res.getLong("sum");
        }else {
            return 0l;
        }
    }

}
