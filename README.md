TestcontainersとLocalStackでAWSサービスを使っててもJUnitでテストする
==============================================================

# これは何？
S3にファイルをアップロードするクラスを、Testcontainers + LocalStackを使ってテストします。

- テスト対象: [FileUploader.java](src/main/java/com/example/FileUploader.java)
- テストコード: [FileUploaderTest.java](src/test/java/com/example/FileUploaderTest.java)

# 環境
- JDK 17
- Docker Desktop
- Maven

# テスト実行方法
IDEで[FileUploaderTest.java](src/test/java/com/example/FileUploaderTest.java)を実行、または `mvn clean test` コマンドを実行

# 注意点
- pom.xmlに `aws-java-sdk-core` の依存性が必要（無いと実行時例外）
- pom.xmlからJUnit 4の依存性を外さない（無いとビルドに失敗）

# 公式ドキュメント
https://www.testcontainers.org/modules/localstack/

> サンプルコードがJUnit 4で書かれているので注意してください。
