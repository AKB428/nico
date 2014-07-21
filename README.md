#Nico

## twitter keyword search and image downloader

![niko](http://i.imgur.com/detsLKI.jpg)


##これはなに？

Twitterをキーワードで検索しヒットしたツイートに画像があったら保存するバッチです。

TwitterのStreaming APIを使用します。


##事前準備

Twitterの開発者アカウントを取得し

* consumer_key
* consumer_secret
* access_token
* access_token_secret

を取得し、conf/twitter_conf.jsonに書いておいてください。


##起動方法

デフォルト

	java -jar nico.jar "ぬこ"

Twitter設定ファイルを指定する

	java -jar nico.jar "ラブライブ" "private/twitter_conf.json"

Java7で起動

	java -jar nico_jvm7.jar "艦これ"


##コンパイル方法

コードは全部Javaで書かれています。

Eclipseでプロジェクト指定するかantでコンパイルしてください。(注:build.xmlおよびJVM7用のjarはあまりメンテしていません)


##実行環境

Java7+ (JVM7.0以上が動くOS)

推奨はJava8


