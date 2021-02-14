package com.fergesch.encaeats.dao;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenericCosmosDao<T> {

    @Autowired
    private CosmosDatabase cosmosDatabase;

    String tableName;
    Class<T> type;
    CosmosContainer container;

    public void init(Class<T> type, String tableName) {
        setType(type);
        setTableName(tableName);
        container = cosmosDatabase.getContainer(tableName);
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<T> getAllType() {
        String sql = "SELECT * from " + tableName;
        return executeSelect(sql);
    }

    public List<T> getFromStringValue(String column, String value) {
        String sql = "SELECT * from " + tableName + " c WHERE c." + column + " = '" + value + "'";
        return executeSelect(sql);
    }

    public List<T> getFromStringValue(Map<String, String> params) {
        ArrayList<String> paramList = new ArrayList<>();
        params.forEach((k, v) -> paramList.add("c." + k + " = '" + v + "'"));
        String paramString = String.join(" and ", paramList);
        String sql = "SELECT * from " + tableName + " c WHERE " + paramString;
        return executeSelect(sql);
    }

    public List<T> multiGet(String column, List<String> values) {
        String sql = "SELECT * from " + tableName + " c WHERE c." + column + " IN ('" + String.join("','", values)
                + "')";
        return executeSelect(sql);

    }

    public List<T> executeSelect(String sql) {
        ArrayList<T> results = new ArrayList<>();
        CosmosPagedIterable<T> queryResults = container.queryItems(sql, new CosmosQueryRequestOptions(), this.type);
        queryResults.forEach(results::add);
        return results;
    }

    public T upsertItem(T item) {
        return container.upsertItem(item).getItem();
    }

    public T insertItem(T item) {
        return container.createItem(item).getItem();
    }

    public void deleteItem(T item) {
        container.deleteItem(item, new CosmosItemRequestOptions());
    }
}
