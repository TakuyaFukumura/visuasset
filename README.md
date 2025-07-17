# visuasset

[![Build](https://github.com/TakuyaFukumura/visuasset/actions/workflows/build.yml/badge.svg)](https://github.com/TakuyaFukumura/visuasset/actions/workflows/build.yml)
[![Java Version](https://img.shields.io/badge/Java-17-blue.svg)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)

資産を可視化するSpring Boot Webアプリケーション

## 概要

visuassetは、個人の資産状況を可視化するためのWebアプリケーションです。
月次・年次の資産推移をグラフやチャートで表示し、資産管理を効率化します。

## 必要な環境

### Docker開発環境の場合
- **Docker**: 最新版
- **Docker Compose**: 最新版

### ローカル開発の場合
- **Java**: 17以上
- **Maven**: 3.6以上（付属のMaven Wrapperでも実行可能）
- **メモリ**: 最低512MB（推奨1GB以上）

## 使用技術

- **フレームワーク**: Spring Boot 3.5.3
- **テンプレートエンジン**: Thymeleaf
- **データベース**: H2 Database（開発用）
- **ORM**: Spring Data JPA
- **ビルドツール**: Maven
- **テストフレームワーク**: Spock Framework（Groovy）
- **その他**: Lombok（定型コード削減）

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
  - JDBC URL: `jdbc:h2:mem:testdb`
  - ユーザー名: `sa`
  - パスワード: (空白)

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

アプリケーションが起動したら、以下のURLにアクセスできます：
- メインアプリケーション: http://localhost:8080
- H2データベースコンソール: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - ユーザー名: `sa`
  - パスワード: (空白)
### コンパイルと実行
```bash
./mvnw clean package
```
```bash
java -jar target/visuasset.jar
```

### テストの実行
```bash
./mvnw test
```

## 機能
- **年次資産管理**
  - 年次資産サマリーの登録・更新
  - 年次資産推移の表示
- **月次資産管理**
  - 現預金、有価証券、暗号資産の月次残高登録
  - 年別の月次データ表示とグラフ化
  - 月次データの編集・削除機能

## プロジェクト構成

```
src/
├── main/
│   ├── java/
│   │   └── com/example/visuasset/
│   │       ├── controller/    # コントローラー層
│   │       ├── service/       # サービス層
│   │       ├── entity/        # エンティティ層
│   │       ├── repository/    # リポジトリ層
│   │       └── Visuasset.java # メインクラス
│   └── resources/
│       ├── static/            # 静的ファイル
│       ├── templates/         # Thymeleafテンプレート
│       ├── sql/               # SQLファイル
│       └── application.properties # 設定ファイル
└── test/
    └── groovy/                # Spockテスト
```
