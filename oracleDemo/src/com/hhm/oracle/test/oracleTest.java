package com.hhm.oracle.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.oracore.OracleType;

/**
 * 测试利用jdbc来操作oracle数据库管理系统
 * @author 黄帅哥
 *
 */
public class oracleTest {
	//建立数据库连接
	private static String username="scott";
	private static String password="3113007968";
	private static Connection conn=null;
	private static String driverName = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	//加载驱动
	static{
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url, username, password);
	}
	
	/**
	 * 查询数据库
	 * @return
	 * @throws SQLException
	 */
	public static void query() throws SQLException{
		Connection conn=getConnection();
		//准备查询语句
		String sql ="select ename,sal from emp";
		
		//准备查询对象
		PreparedStatement ps=conn.prepareStatement(sql);
		//用结果集来存储返回的数据
		ResultSet rs=ps.executeQuery();
		
		//输出
		while(rs.next()){
			//rs每一条数据的索引是从1开始
			String ename=rs.getString(1);
			int salary=rs.getInt(2);
			
			System.out.println("姓名是："+ename+"，工资是："+salary);
		}
	}
	
	/**
	 * 调用存储过程
	 * @throws SQLException 
	 */
	public static void testProcedure() throws SQLException{
		Connection conn=getConnection();
		
		//准备调用存储过程的对象，并且决定调用哪个存储过程
		CallableStatement callableStatement=conn.prepareCall("{call p_stats(?,?)}");
		
		//设置第一个输入占位符的值
		callableStatement.setInt(1, 7499);
		//设置第二个占位符的输出参数的类型
		callableStatement.registerOutParameter(2, OracleType.STYLE_INT);
		
		//执行过程
		callableStatement.execute();
		
		//获取结果
		int salary=callableStatement.getInt(2);
		
		System.out.println(salary);
	}
	
	/**
	 * 调用存储函数
	 * @throws SQLException
	 */
	public static void testFunction() throws SQLException{
		Connection conn=getConnection();
		
		//准备调用存储过程的对象，并且决定调用哪个存储过程
		CallableStatement callableStatement=conn.prepareCall("{?=call f_yearSalary(?)}");
		
		//设置第一个占位符的输出参数的类型
			callableStatement.registerOutParameter(1, OracleType.STYLE_INT);
			
		//设置第二个输入占位符的值
		callableStatement.setInt(2, 7499);
		
		
		//执行过程
		callableStatement.execute();
		
		//获取结果
		int salary=callableStatement.getInt(1);
		
		System.out.println(salary);
	}
	
	public static void main(String[] args) throws SQLException {
		//query();
		//testProcedure();
		testFunction();
	}
	
}
