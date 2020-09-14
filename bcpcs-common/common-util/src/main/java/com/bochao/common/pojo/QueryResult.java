package com.bochao.common.pojo;

import java.util.List;

public class QueryResult<T> {
	private long total;//总的记录数
	private List<T> rows;//结果集
	
	public QueryResult() {}
	public QueryResult(long total, List<T> rows){
		this.total=total;
		this.rows=rows;
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
