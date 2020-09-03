package com.wimp.dreamer.base.msg;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @param <T>
 * @author zy
 * table类型返回值
 */
@Getter
@Setter
public class TableResultResponse<T> extends BaseResponse {

    private TableData<T> data;

    public TableResultResponse(long total, List<T> rows) {
        this.data = new TableData<>(total, rows);
    }

    public TableResultResponse() {
        this.data = new TableData<>();
    }

    public TableResultResponse<T> total(int total) {
        this.data.setTotal(total);
        return this;
    }

    public TableResultResponse<T> total(List<T> rows) {
        this.data.setRows(rows);
        return this;
    }

    static class TableData<T> {
        long total;
        List<T> rows;

        public TableData(long total, List<T> rows) {
            this.total = total;
            this.rows = rows;
        }

        public TableData() {
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public List<T> getRows() {
            return rows;
        }

        public void setRows(List<T> rows) {
            this.rows = rows;
        }
    }
}
