package jp.co.kiramex.dbSample.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;	//←修正
import java.sql.ResultSet;
import java.sql.SQLException;


public class DbConnectSample05 {

	public static void main(String[] args) {
		// 3. データベース接続と結果取得のための変数宣言
		Connection con = null;
		PreparedStatement spstmt = null;	// 更新前、更新後の検索用プリペアードステートメントオブジェクト
		PreparedStatement ipstmt = null;    // 更新処理用プリペアードステートメントオブジェクト
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
			// 検索用SQLおよび検索用PreparedStatementオブジェクトを取得
			String sql = "SELECT * FROM city WHERE CountryCode = ?";    // ← 修正
			spstmt = con.prepareStatement(sql);

			// 更新するCountryCodeを入力
			System.out.println("CountryCodeを入力してください > ");	// ←追記
			String str1 = keyIn();

			// 入力されたCountryCodeをPreparedStatementオブジェクトにセット
			spstmt.setString(1, str1);	//←追記


			// 5, 6. Select文の実行と結果を格納／代入
			rs = spstmt.executeQuery();// ← 修正


			// 7-1. 更新前の結果を表示する	rs.next()⇒表の中に「次の行のデータ」があるかどうかを指し示すための処理
			System.out.println("更新前===================");
			while(rs.next()) {
				//Name列の値を取得
				String name = rs.getString("Name");
				// CountryCode列の値を取得
				String countryCode = rs.getString("CountryCode");
				// District列の値を取得
				String district = rs.getString("District");
				//Population列の値を取得　←追記
				int population = rs.getInt("Population"); //　←追記

				//取得した値を表示
				System.out.println(name + "\t" + countryCode + "\t" + district + "\t" + population);
			}

			// 7-2. 更新処理を行なう
			System.out.println("更新処理実行=============");

			// 更新用SQLおよび更新用PreparedStatementオブジェクトを取得
			String insertSql = "INSERT INTO city (Name,CountryCode,District,Population) VALUES ('Rafah',?,'Rafah',?)";
			ipstmt = con.prepareStatement(insertSql);

			// 更新するPopulationを入力
			System.out.println("Populationを数字で入力してください　> ");
			int num1 = keyInNum();

			// 入力されたPopulationとCountryCodeをPreparedStatementオブジェクトにセット
			ipstmt.setString(1,  str1);
			ipstmt.setInt(2, num1);

			// update処理の実行および更新された行数を取得
			int count = ipstmt.executeUpdate();
			System.out.println("更新行数" + count);

			// 7-3. 更新後の結果を表示する
			rs.close();	// 更新後の検索のため、一旦閉じる（閉じないと警告が出るため）
			System.out.println("更新後=================");
			// 検索の再実行と結果を格納／代入
			rs = spstmt.executeQuery();
			while(rs.next()) {
				// Name列の値を取得
				String name = rs.getString("Name");
				// CountryCode列の値を取得
				String countryCode = rs.getString("CountryCode");
				// District列の値を取得
				String district = rs.getString("District");
				// Population列の値を取得
				int population = rs.getInt("population");
				// 取得した値を表示
				System.out.print(name + "\t" + countryCode + "\t" + district + "\t" + population);
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
			if(ipstmt != null ) {
				try {
				ipstmt.close();

			}catch(SQLException e) {
				System.err.println("PreparedStatementを閉じるときにエラーが発生しました。");
				e.printStackTrace();
				}
			}
			if(spstmt != null) {
				try {
					spstmt.close();
				}catch(SQLException e) {
					System.out.println("PreparedStatementを閉じるときにエラーが発生しました。");
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

     /*
      * キーボードから入力された値をintで返す 引数：なし 戻り値：int
      */
     private static int keyInNum() {
    	 int result = 0;
    	 try {
    		 result = Integer.parseInt(keyIn());

    	 }catch(NumberFormatException e) {

    	 }
    	 return result;
     }

}
