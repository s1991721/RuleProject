package com.ljf.ruleproject.util;

import com.ljf.ruleproject.poet.Store;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.lin on 2020/7/1
 */
public class ResultSetToBean {

    public static List<Store> coverList(ResultSet resultSet) throws SQLException {
        List<Store> dataList = new ArrayList();

        while (resultSet.next()) {
            Store data = new Store();
            data.setId(resultSet.getString("id"));
            data.setSignValue(resultSet.getInt("sign_value"));
            data.setAttribute(resultSet.getString("attribute"));
            data.setFigure(resultSet.getString("figure"));
            data.setSize(resultSet.getString("size"));
            data.setSales(resultSet.getInt("sales"));
            data.setReturns(resultSet.getInt("returns"));
            dataList.add(data);
        }
        return dataList;
    }

}
