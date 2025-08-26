# visuasset - GitHub Copilot 指示書

**常にこれらの指示に最初に従い、ここにある情報が不完全であるか誤りが見つかった場合のみ、追加の検索やコンテキスト収集に頼ってください。**

visuassetは、資産の可視化と管理のためのSpring Boot 3.5.4 Webアプリケーションです。このアプリケーションにより、ユーザーは年間および月間の資産を追跡し、ポートフォリオを可視化し、投資シミュレーションを実行できます。

## 言語およびコミュニケーションポリシー

**すべての応答とコミュニケーションは日本語で行う必要があります**：
- すべての説明、コメント、提案は日本語で書く
- コードコメントは日本語で書く
- 変数名とメソッド名は英語でも構わないが、説明は日本語で行う
- エラーメッセージと技術的説明は日本語で提供する

## 効果的な作業方法

### ブートストラップとビルド
プロジェクトのセットアップとビルドのために、以下のコマンドを順番に実行してください：

```bash
# プロジェクトルートに移動
cd /path/to/visuasset

# アプリケーションをビルド - キャンセル禁止：初回ビルドは90秒以上、その後のビルドは10-15秒程度
./mvnw clean package
```

**重要なタイミング**：`./mvnw clean package`の初回実行時は120分以上のタイムアウトを設定してください。ビルドは依存関係をダウンロードするため、初回は60-90秒かかる場合があります。

### テストの実行
```bash
# 全テストを実行 - キャンセル禁止：約15秒かかります。いくつかのテスト失敗は存在する可能性があります（重要ではありません）
./mvnw test
```

**重要なタイミング**：テストコマンドには30分以上のタイムアウトを設定してください。テストは7-15秒で完了します。

### アプリケーションの実行

#### ローカル開発（推奨）
```bash
# Spring Bootアプリケーションを起動 - キャンセル禁止：起動には約5秒かかります
./mvnw spring-boot:run
```
**重要なタイミング**：30分以上のタイムアウトを設定してください。アプリケーションは2-5秒で起動します。

#### Docker開発
```bash
# 最初にアプリケーションをビルド（Docker実行前に必要）
./mvnw clean package -DskipTests

# Dockerコンテナのビルドと起動 - キャンセル禁止：合計約10秒かかります
docker compose up --build
```

**重要なタイミング**：Dockerコマンドには30分以上のタイムアウトを設定してください。Dockerのビルドと起動は5-10秒で完了します。

### アクセスポイント
アプリケーション起動後：
- **メインアプリケーション**：http://localhost:8080（/yearlyにリダイレクト）
- **H2データベースコンソール**：http://localhost:8080/h2-console
  - JDBC URL：`jdbc:h2:./.db/dev/h2`（ローカル）または`jdbc:h2:mem:testdb`（Docker）
  - ユーザー名：`sa`
  - パスワード：（空白のまま）

### サービスの停止
```bash
# Spring Bootの停止（実行中のターミナルでCtrl+C）

# Dockerコンテナの停止
docker compose down
```

## 検証シナリオ

**変更後は常に以下の検証シナリオを実行してください：**

### 基本的なアプリケーション検証
1. ローカルメソッドを使用してアプリケーションをビルド・起動
2. メインページの読み込みを確認：`curl -I http://localhost:8080`がHTTP 302リダイレクトを返すことを確認
3. 主要なエンドポイントをテスト：
   - 年間資産：http://localhost:8080/yearly
   - ポートフォリオ：http://localhost:8080/portfolio
   - シミュレーション：http://localhost:8080/simulation
   - 月間資産：http://localhost:8080/monthly
4. H2コンソールのアクセス可能性を確認：http://localhost:8080/h2-console

### 完全なユーザーワークフロー検証
1. 年間資産ページ（/yearly）に移動
2. 年間資産データを追加または表示
3. シミュレーションページ（/simulation）に移動
4. デフォルトパラメータで投資シミュレーションを実行
5. ポートフォリオページ（/portfolio）に移動
6. データ可視化が正しく表示されることを確認

### Docker検証
1. 実行中のローカルSpring Bootインスタンスを停止
2. アプリケーションをビルド：`./mvnw clean package -DskipTests`
3. Dockerを起動：`docker compose up --build`
4. http://localhost:8080でアプリケーションのアクセス可能性を確認
5. Dockerを停止：`docker compose down`

## CI互換性

GitHub Actions CI（.github/workflows/build.yml）は以下を実行します：
```bash
mvn clean package
```

**コミット前に、この正確なコマンドで変更がパスすることを常に確認してください。**

## プロジェクト構造

### 主要ディレクトリ
```
src/
├── main/java/com/example/visuasset/
│   ├── controller/          # Webコントローラ（5コントローラ）
│   ├── service/             # ビジネスロジックサービス
│   ├── entity/              # JPAエンティティ
│   ├── repository/          # データリポジトリ
│   └── dto/                 # データ転送オブジェクト
├── main/resources/
│   ├── templates/           # Thymeleafテンプレート（11テンプレート）
│   ├── static/              # CSS、JS、画像
│   ├── sql/                 # データベース初期化スクリプト
│   └── application.properties
└── test/groovy/             # Spockフレームワークテスト（21テストファイル）
```

### 主要コントローラ
- **YearlyAssetsController**：メインランディングページ、年間資産管理
- **SimulationController**：投資シミュレーション機能
- **PortfolioController**：資産ポートフォリオ可視化
- **MonthlyAssetsController**：月間資産追跡
- **GoalAchievementController**：財務目標追跡

## 技術スタック

- **Java 17**（必須）
- **Spring Boot 3.5.4**
- **Maven 3.6+**（ラッパー提供）
- **H2 Database**（Dockerではインメモリ、ローカルではファイルベース）
- **Thymeleaf** テンプレート
- **Lombok** コード生成用
- **Spock Framework**（Groovy）テスト用

## 一般的な問題と解決策

### ビルドの問題
- Mavenラッパーのダウンロードが失敗する場合：インターネット接続を確認
- 依存関係解決が失敗する場合：`rm -rf ~/.m2/repository`でMavenキャッシュをクリア

### データベースの問題
- ローカル開発ではデータベースファイルが`.db/dev/`に保存される
- Dockerでは`db-data/`ボリュームにデータが永続化される
- データベースは初回起動時に`src/main/resources/sql/`を使用して自動初期化される

### アプリケーションが起動しない場合
- Java 17がインストールされていることを確認：`java -version`
- ポート8080が使用されていないことを確認：`lsof -i :8080`
- 具体的なエラーメッセージについてアプリケーションログを確認

## 開発ワークフロー

1. **ソースコードを変更**
2. **ビルド**：`./mvnw clean package`（初回は90秒のタイムアウト）
3. **テスト**：`./mvnw test`（30秒のタイムアウト）
4. **ローカル実行**：`./mvnw spring-boot:run`（30秒のタイムアウト）
5. **検証**：主要エンドポイントとユーザーワークフローをテスト
6. **Docker用**：`./mvnw clean package -DskipTests`で再ビルド後、`docker compose up --build`

## メモリ要件
- **最小**：512MB RAM
- **推奨**：1GB以上のRAM（開発用）

## 重要な注意事項
- **ビルドやテストコマンドは絶対にキャンセルしない** - ハングしているように見えても処理中です
- **コード変更後は常に機能を手動で検証する**
- **適切なタイムアウトを設定する**：ビルドには120分以上、テストと起動には30分以上
- **インフラストラクチャ変更時はローカルとDockerの両方のデプロイメント方法をテストする**
