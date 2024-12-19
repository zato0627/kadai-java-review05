package jp.co.kiramex.dbSample.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Review05 {

	public static void main(String[] args) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/kadaidb?useSSL=false&allowPublicKeyRetrieval=true",
					"root",
					"ayuchan0627"
					);
			String sql = "SELECT * FROM person  WHERE id = ?";
			pstmt = con.prepareStatement(sql);

			System.out.println("検索キーワードを入力してください > ");
			int input = keyInNum();

			pstmt.setInt(1, input);

			rs = pstmt.executeQuery();

			while(rs.next()) {
				String name = rs.getString("Name");	//Name列の値を取得

				int age = rs.getInt("age");	//age列の値を取得

				System.out.println(name);
				System.out.println(age);
			}

		}catch(ClassNotFoundException e) {
			System.err.println("JDBCドライバのロードに失敗しました。");
			e.printStackTrace();
		}catch(SQLException e) {
			System.err.println("データベースに異常が発生しました。");
			e.printStackTrace();
		}finally {

			if(rs != null) {
				try {
					rs.close();
				}catch(SQLException e) {
					System.err.println("ResultSetを閉じるときにエラーが発生しました。");
					e.printStackTrace();
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				}catch(SQLException e) {
					System.err.println("PreparedStatementを閉じるときにエラーが発生しました。");
					e.printStackTrace();
				}
			}
			if(con != null) {
				try {
					con.close();
				}catch(SQLException e) {
					System.err.println("データベース切断時にエラーが発生しました。");
					e.printStackTrace();
				}
			}
		}

	}

	private static String keyIn() {
		String line = null;
		try {
			BufferedReader key = new BufferedReader(new InputStreamReader(System.in));
			line = key.readLine();
		}catch(IOException e) {

		}
		return line;
	}
	private static int keyInNum() {
		int result = 0;
		try {
			result = Integer.parseInt(keyIn());
		}catch(NumberFormatException e) {

		}
		return result;
	}

}
