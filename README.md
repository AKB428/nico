#Nico

## twitter keyword search and image downloader

![niko](http://i.imgur.com/detsLKI.jpg)


##これはなに？

Twitterをキーワードで検索しヒットしたツイートに画像があったら保存するバッチです。

TwitterのStreaming APIを使用します。

##リリース記事

http://akb428.hatenablog.com/entries/2014/07/23

##事前準備

### application.properties

#### Twitter開発者アカウント設定

Twitterの開発者アカウントを取得し

* consumer_key
* consumer_secret
* access_token
* access_token_secret

を取得し、config/application.properties に書いておいてください。
このgitをcommitしたりする人はcp private/　にコピーし編集してください。

#### 検索キーワード設定

twitter.searchKeywordにStreamingAPIに渡す検索ワードを指定してください。

##起動方法

デフォルト

	java -jar nico.jar

設定ファイルを指定する

	java -jar nico.jar private/application.properties



##コンパイル方法

コードは全部Javaで書かれています。

Eclipseでプロジェクト指定するかantでコンパイルしてください。(注:build.xmlはあまりメンテしていません)


##実行環境

Java8+


### 外部サーバー

設定ファイルで外部サーバーの使用を許可した場合はそのソフトウェアが必要になります

対応しているサーバーは以下になります。

#### MessageQueue Server
* RabbitMQ

#### Rerational Database Server
* H2
* MariaDB


