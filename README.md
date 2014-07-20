#TwitterKeyWordSearch

##これはなに？

Twitterをキーワード指定で検索するツールです

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

	java -jar tkws.jar "hogehoge"

Twitter設定ファイルを指定する

	java -jar tkws.jar "ラブライブ" "private/twitter_conf.json"