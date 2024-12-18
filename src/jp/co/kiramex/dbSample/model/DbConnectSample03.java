package jp.co.kiramex.dbSample.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnectSample03 {

	public static void main(String[] args) {
		// 3. データベース接続と結果取得のための変数宣言
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;


        try {
			// 1. ドライバのクラスをJava上で読み込む
			Class.forName("com.mysql.cj.jdbc.Driver");


			// 2. DBと接続する
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/world?useSSL=false&allowPublicKeyRetrieval=true",
					"root",
					"ayuchan0627"
					);

			// 4. DBとやりとりする窓口（Statementオブジェクト）の作成
			stmt = con.createStatement();

			// 5, 6. Select文の実行と結果を格納／代入
			System.out.println("検索キーワードを入力してください > ");	// ←追記
			String imput = keyIn();	// ←追記

			String sql = "select * from country where Name = '" + imput + "'";	//←修正
			rs = stmt.executeQuery(sql);


			// 7. 結果を表示する	rs.next()⇒表の中に「次の行のデータ」があるかどうかを指し示すための処理
			while(rs.next()) {
				//Name列の値を取得
				String name = rs.getString("Name");
				//Population列の値を取得　←追記
				int population = rs.getInt("Population"); //　←追記

				//取得した値を表示
				System.out.println(name);
				System.out.println(population);
			}


		} catch (ClassNotFoundException e) {

			System.err.println("JDBCドライバーのロードに失敗しました。");	//out ではなく err というstaticなフィールドを使用することで、STS（Eclipse）のコンソールでは、自動的に赤字で表示されます。
			e.printStackTrace();	//メソッドがどの順番に呼び出されたのかをトレースし、出力する メソッドとなります。デバッグ時にはよく使用

		} catch (SQLException e) {

			System.err.println("データベースに異常が発生しました。");
			e.printStackTrace();

		}finally {
			// 8. 接続を閉じる
			if(rs != null) {
				try{
					rs.close();

				}catch(SQLException e) {
					System.err.println("ResultSetを閉じるときにエラーが発生しました。");
					e.printStackTrace();

				}
			}
			if(stmt != null ) {
				try {
				stmt.close();

			}catch(SQLException e) {
				System.err.println("Statementを閉じるときにエラーが発生しました。");
				e.printStackTrace();
				}
			}
			if(con != null ) {
				try {
					con.close();
				} catch(SQLException e) {
					System.err.println("データベース切断時にエラーが発生しました。");
					e.printStackTrace();
				}
			}
		}
	}
    /*
     * キーボードから入力された値をStringで返す　引数：なし　戻り値：入力された文字		//←追記
     */
     private static String keyIn() {
    	 String line = null;
    	 try {
    		 BufferedReader key = new BufferedReader(new InputStreamReader(System.in));
    		 line = key.readLine();
    	 }catch(IOException e) {

    	 }
    	 return line;

	}

}
