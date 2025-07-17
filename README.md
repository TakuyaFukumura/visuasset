# visuasset
資産を可視化するアプリ

## Docker開発環境でのセットアップ

### 前提条件
- Docker
- Docker Compose

### セットアップと起動手順

1. リポジトリをクローン
```bash
git clone https://github.com/TakuyaFukumura/visuasset.git
cd visuasset
```

2. アプリケーションをビルド
```bash
./mvnw clean package -DskipTests
```

3. Dockerコンテナをビルドして起動
```bash
docker compose up --build
```

4. アプリケーションにアクセス
- アプリケーション: http://localhost:8080
- H2データベースコンソール: http://localhost:8080/h2-console

### コンテナの停止
```bash
docker compose down
```

### データベースファイルについて
- H2データベースファイルは `db-data` フォルダに永続化されます
- 初回起動時に自動でデータベースが初期化されます

### 開発時のワークフロー
1. ソースコードを変更
2. `./mvnw clean package -DskipTests` でリビルド
3. `docker compose up --build` でコンテナを再起動

## ローカル開発（Docker不使用）

### 起動
```bash
./mvnw spring-boot:run
```

### コンパイルと実行
```bash
./mvnw clean package
```
```bash
java -jar target/visuasset.jar
```
